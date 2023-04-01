package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.PHOTOS_PER_PAGE
import de.memorian.wearos.marsrover.data.RoverPhotosAreEmptyException
import de.memorian.wearos.marsrover.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.domain.model.MarsRoverImageUrl
import de.memorian.wearos.marsrover.domain.model.RoverManifests
import de.memorian.wearos.marsrover.domain.util.RandomNumberGenerator
import javax.inject.Inject

private const val SIZE_ROVER_MANIFESTS = 3

/**
 * Selects a new random image by
 * - selecting a random mission manifest (random rover)
 * - selecting a random sol (photos from that sol)
 * - selecting a random image from that sol
 *
 * and calls the repository with the random parameters.
 */
class RefreshStoredDailyImageAction @Inject constructor(
    private val roverPhotosRepository: RoverPhotosRepository,
    private val getRoverManifestsAction: GetRoverManifestsAction,
    private val randomNumberGenerator: RandomNumberGenerator,
) : CoroutineUseCase<Unit, MarsRoverImageUrl> {

    override suspend fun execute(params: Unit): Result<MarsRoverImageUrl> {
        getRoverManifestsAction.execute(Unit)
            .fold(
                onFailure = {
                    return Result.failure(it)
                },
                onSuccess = { roverManifests ->
                    return refreshImage(roverManifests)
                }
            )
    }

    /**
     * Refreshes the daily image. Sometimes the NASA API returns an image with a
     * "http://" URL, which is blocked by Android network unless we allow cleartext
     * traffic. To circumvent this, we refresh the image again in such a case.
     */
    private suspend fun refreshImage(roverManifests: RoverManifests): Result<MarsRoverImageUrl> {
        val roverManifest =
            when (randomNumberGenerator.generate(SIZE_ROVER_MANIFESTS - 1)) {
                0 -> roverManifests.curiosityManifest
                1 -> roverManifests.opportunityManifest
                else -> roverManifests.spiritManifest
            }

        val sol = randomNumberGenerator.generate(roverManifest.maxSol)
        val photosFromSol = roverManifest.photos[sol]
        val lastAvailableIndex = minOf(photosFromSol.totalPhotos, PHOTOS_PER_PAGE)
        val index = randomNumberGenerator.generate(lastAvailableIndex)

        return roverPhotosRepository.fetchAndPersistNewDailyImage(
            roverType = roverManifest.roverType,
            sol = sol,
            index = index,
        ).mapCatching {
            if (it.startsWith("http://")) {
                return@mapCatching refreshImage(roverManifests).getOrThrow()
            } else {
                return@mapCatching it
            }
        }.fold(
            onSuccess = {
                return@fold Result.success(it)
            },
            onFailure = {
                return@fold if (it is RoverPhotosAreEmptyException) {
                    refreshImage(roverManifests)
                } else {
                    Result.failure(it)
                }
            }
        )
    }
}