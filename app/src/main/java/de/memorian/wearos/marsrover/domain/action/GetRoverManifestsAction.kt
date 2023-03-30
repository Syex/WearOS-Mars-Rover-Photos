package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.MissionManifestRepository
import de.memorian.wearos.marsrover.domain.action.GetRoverManifestsAction.RoverManifests
import de.memorian.wearos.marsrover.domain.model.MarsRoverMissionManifest
import javax.inject.Inject

class GetRoverManifestsAction @Inject constructor(
    private val missionManifestRepository: MissionManifestRepository,
) : CoroutineUseCase<Unit, Result<RoverManifests>> {

    override suspend fun execute(params: Unit): Result<RoverManifests> {
        return try {
            val curiosityManifest = missionManifestRepository.getCuriosityManifest()
            val opportunityManifest = missionManifestRepository.getOpportunityManifest()
            val spiritManifest = missionManifestRepository.getSpiritManifest()

            Result.success(
                RoverManifests(curiosityManifest, opportunityManifest, spiritManifest)
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    data class RoverManifests(
        val curiosityManifest: MarsRoverMissionManifest,
        val opportunityManifest: MarsRoverMissionManifest,
        val spiritManifest: MarsRoverMissionManifest,
    )
}