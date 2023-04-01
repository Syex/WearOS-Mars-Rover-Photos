package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.MissionManifestRepository
import de.memorian.wearos.marsrover.domain.model.RoverManifests
import javax.inject.Inject

class GetRoverManifestsAction @Inject constructor(
    private val missionManifestRepository: MissionManifestRepository,
) : CoroutineUseCase<Unit, RoverManifests> {

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

}