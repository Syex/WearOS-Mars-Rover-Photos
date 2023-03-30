package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.domain.model.RoverManifests
import de.memorian.wearos.marsrover.domain.model.RoverType
import de.memorian.wearos.marsrover.domain.util.RandomNumberGenerator
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshStoredDailyImageActionTest {

    private val roverPhotosRepository = mockk<RoverPhotosRepository>()
    private val randomNumberGenerator = mockk<RandomNumberGenerator>()
    private val refreshStoredDailyImageAction =
        RefreshStoredDailyImageAction(roverPhotosRepository, randomNumberGenerator)

    @Test
    fun `execute with valid input should return a success result`() = runTest {
        val sol = 1000

        val roverManifests = RoverManifests(
            curiosityManifest = mockk {
                every { roverType } returns RoverType.Curiosity
                every { maxSol } returns sol
                every { photos[sol] } returns mockk {
                    every { totalPhotos } returns 1500
                }
            },
            opportunityManifest = mockk(),
            spiritManifest = mockk(),
        )
        val expectedUrl = "https://marsrover.nasa.gov/image.jpg"
        val index = 5
        val roverType = RoverType.Curiosity
        coEvery {
            roverPhotosRepository.fetchAndPersistNewDailyImage(
                roverType,
                sol,
                index
            )
        } returns Result.success(expectedUrl)
        every { randomNumberGenerator.generate(any()) } returns 0 andThen sol andThen index

        val result = refreshStoredDailyImageAction.execute(
            RefreshStoredDailyImageAction.Params(roverManifests)
        )

        result.shouldBeSuccess()
        result.getOrThrow() shouldBe expectedUrl
    }
}