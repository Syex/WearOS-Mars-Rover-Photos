package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.PHOTOS_PER_PAGE
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
    private val randomNumberGenerator: RandomNumberGenerator,
) : CoroutineUseCase<RefreshStoredDailyImageAction.Params, Result<MarsRoverImageUrl>> {

    override suspend fun execute(params: Params): Result<MarsRoverImageUrl> {
        val roverManifest = when (randomNumberGenerator.generate(SIZE_ROVER_MANIFESTS - 1)) {
            0 -> params.roverManifests.curiosityManifest
            1 -> params.roverManifests.opportunityManifest
            else -> params.roverManifests.spiritManifest
        }

        val sol = randomNumberGenerator.generate(roverManifest.maxSol)
        val photosFromSol = roverManifest.photos[sol]
        val lastAvailableIndex = minOf(photosFromSol.totalPhotos, PHOTOS_PER_PAGE)
        val index = randomNumberGenerator.generate(lastAvailableIndex)

        return roverPhotosRepository.fetchAndPersistNewDailyImage(
            roverType = roverManifest.roverType,
            sol = sol,
            index = index,
        )
    }

    data class Params(val roverManifests: RoverManifests)
}