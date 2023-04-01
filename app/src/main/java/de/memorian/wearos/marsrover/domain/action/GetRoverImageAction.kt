package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.domain.model.MarsRoverImageUrl
import javax.inject.Inject

class GetRoverImageAction @Inject constructor(
    private val roverPhotosRepository: RoverPhotosRepository,
    private val getRoverManifestsAction: GetRoverManifestsAction,
    private val refreshStoredDailyImageAction: RefreshStoredDailyImageAction,
) : CoroutineUseCase<Unit, MarsRoverImageUrl> {

    override suspend fun execute(params: Unit): Result<MarsRoverImageUrl> {
        roverPhotosRepository.getPersistedDailyImageUrl()?.let { storedImageUrl ->
            return Result.success(storedImageUrl)
        }

        getRoverManifestsAction.execute(Unit)
            .fold(
                onFailure = {
                    return Result.failure(it)
                },
                onSuccess = { roverManifests ->
                    return refreshStoredDailyImageAction.execute(
                        RefreshStoredDailyImageAction.Params(roverManifests)
                    )
                }
            )
    }
}