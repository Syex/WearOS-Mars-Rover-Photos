package de.memorian.wearos.marsrover.app.domain.action

import de.memorian.wearos.marsrover.app.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.app.domain.model.MarsRoverImageUrl
import javax.inject.Inject

class GetRoverImageAction @Inject constructor(
    private val roverPhotosRepository: RoverPhotosRepository,
    private val refreshStoredDailyImageAction: RefreshStoredDailyImageAction,
) : CoroutineUseCase<Unit, MarsRoverImageUrl> {

    override suspend fun execute(params: Unit): Result<MarsRoverImageUrl> {
        roverPhotosRepository.getPersistedDailyImageUrl()?.let { storedImageUrl ->
            return Result.success(storedImageUrl)
        }

        return refreshStoredDailyImageAction.execute(Unit)
    }
}