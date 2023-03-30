package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.MissionManifestRepository
import de.memorian.wearos.marsrover.domain.model.MarsRoverMissionManifest
import de.memorian.wearos.marsrover.domain.model.RoverManifests
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRoverManifestsActionTest {

    private val missionManifestRepository = mockk<MissionManifestRepository>()
    private val getRoverManifestsAction = GetRoverManifestsAction(missionManifestRepository)

    @Test
    fun `execute should return success result with rover manifests`() = runTest {
        val curiosityManifest = mockk<MarsRoverMissionManifest>()
        val opportunityManifest = mockk<MarsRoverMissionManifest>()
        val spiritManifest = mockk<MarsRoverMissionManifest>()
        coEvery { missionManifestRepository.getCuriosityManifest() } returns curiosityManifest
        coEvery { missionManifestRepository.getOpportunityManifest() } returns opportunityManifest
        coEvery { missionManifestRepository.getSpiritManifest() } returns spiritManifest

        val result = getRoverManifestsAction.execute(Unit)

        result.shouldBeSuccess()
        result.getOrNull() shouldBe RoverManifests(
            curiosityManifest,
            opportunityManifest,
            spiritManifest
        )
    }

    @Test
    fun `execute should return failure result if an exception is thrown`() = runTest {
        val exception = Exception("Something went wrong")
        coEvery { missionManifestRepository.getCuriosityManifest() } throws exception

        val result = getRoverManifestsAction.execute(Unit)

        result.shouldBeFailure<Exception>()
        result.exceptionOrNull() shouldBe exception
    }
}