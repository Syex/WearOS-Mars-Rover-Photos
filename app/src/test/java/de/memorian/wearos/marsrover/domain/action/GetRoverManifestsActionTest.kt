package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.MarsRoverRepository
import de.memorian.wearos.marsrover.domain.model.MarsRoverMissionManifest
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

    private val marsRoverRepository = mockk<MarsRoverRepository>()
    private val getRoverManifestsAction = GetRoverManifestsAction(marsRoverRepository)

    @Test
    fun `execute should return success result with rover manifests`() = runTest {
        val curiosityManifest = mockk<MarsRoverMissionManifest>()
        val opportunityManifest = mockk<MarsRoverMissionManifest>()
        val spiritManifest = mockk<MarsRoverMissionManifest>()
        coEvery { marsRoverRepository.getCuriosityManifest() } returns curiosityManifest
        coEvery { marsRoverRepository.getOpportunityManifest() } returns opportunityManifest
        coEvery { marsRoverRepository.getSpiritManifest() } returns spiritManifest

        val result = getRoverManifestsAction.execute(Unit)

        result.shouldBeSuccess()
        result.getOrNull() shouldBe GetRoverManifestsAction.RoverManifests(
            curiosityManifest,
            opportunityManifest,
            spiritManifest
        )
    }

    @Test
    fun `execute should return failure result if an exception is thrown`() = runTest {
        val exception = Exception("Something went wrong")
        coEvery { marsRoverRepository.getCuriosityManifest() } throws exception

        val result = getRoverManifestsAction.execute(Unit)

        result.shouldBeFailure<Exception>()
        result.exceptionOrNull() shouldBe exception
    }
}