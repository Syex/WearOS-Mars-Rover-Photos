package de.memorian.wearos.marsrover.app.domain.action

import de.memorian.wearos.marsrover.app.data.PHOTOS_PER_PAGE
import de.memorian.wearos.marsrover.app.data.RoverPhotosAreEmptyException
import de.memorian.wearos.marsrover.app.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.app.domain.model.MarsRoverImageUrl
import de.memorian.wearos.marsrover.app.domain.model.RoverManifests
import de.memorian.wearos.marsrover.app.domain.util.RandomNumberGenerator
import timber.log.Timber
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

    private suspend fun refreshImage(roverManifests: RoverManifests): Result<MarsRoverImageUrl> {
        val roverManifest =
            when (randomNumberGenerator.generate(SIZE_ROVER_MANIFESTS - 1)) {
                0 -> roverManifests.curiosityManifest
                1 -> roverManifests.opportunityManifest
                else -> roverManifests.spiritManifest
            }

        val solIndex = randomNumberGenerator.generate(roverManifest.photos.size)
        val photosFromSol = roverManifest.photos[solIndex]
        val lastAvailableIndex = minOf(photosFromSol.totalPhotos, PHOTOS_PER_PAGE)
        val index = randomNumberGenerator.generate(lastAvailableIndex)

        return roverPhotosRepository.fetchAndPersistNewDailyImage(
            roverType = roverManifest.roverType,
            sol = solIndex,
            index = index,
        ).fold(
            onSuccess = {
                return@fold Result.success(it)
            },
            onFailure = {
                return@fold if (it is RoverPhotosAreEmptyException) {
                    Timber.v("Rover photos were empty, refreshing again")
                    refreshImage(roverManifests)
                } else {
                    Result.failure(it)
                }
            }
        )
    }
}