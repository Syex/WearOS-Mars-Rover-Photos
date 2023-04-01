package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.RoverPhotosAreEmptyException
import de.memorian.wearos.marsrover.data.RoverPhotosRepository
import de.memorian.wearos.marsrover.domain.model.MarsRoverMissionManifest
import de.memorian.wearos.marsrover.domain.model.RoverManifests
import de.memorian.wearos.marsrover.domain.model.RoverType
import de.memorian.wearos.marsrover.domain.util.RandomNumberGenerator
import io.kotest.matchers.result.shouldBeFailure
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
    private val getRoverManifestsAction = mockk<GetRoverManifestsAction>()
    private val refreshStoredDailyImageAction =
        RefreshStoredDailyImageAction(
            roverPhotosRepository,
            getRoverManifestsAction,
            randomNumberGenerator
        )

    @Test
    fun `execute with valid input should return a success result`() = runTest {
        prepareSuccessfulRepositoryResponse()
        val expectedUrl = "https://marsrover.nasa.gov/image.jpg"

        coEvery {
            roverPhotosRepository.fetchAndPersistNewDailyImage(any(), any(), any())
        } returns Result.success(expectedUrl)

        val result = refreshStoredDailyImageAction.execute(Unit)

        result.shouldBeSuccess()
        result.getOrThrow() shouldBe expectedUrl
    }

    @Test
    fun `execute should forward failure from getRoverManifestsAction`() = runTest {
        val exception = Exception()
        coEvery { getRoverManifestsAction.execute(Unit) } returns Result.failure(exception)

        val result = refreshStoredDailyImageAction.execute(Unit)

        result.shouldBeFailure()
        result.exceptionOrNull() shouldBe exception
    }

    @Test
    fun `execute should refresh image again if repository returns an image URL starting with http`() =
        runTest {
            prepareSuccessfulRepositoryResponse()

            val notAllowedUrl = "http://marsrover.nasa.gov/image.jpg"
            val expectedUrl = "https://marsrover.nasa.gov/image.jpg"
            coEvery {
                roverPhotosRepository.fetchAndPersistNewDailyImage(any(), any(), any())
            } returns Result.success(notAllowedUrl) andThen Result.success(expectedUrl)

            val result = refreshStoredDailyImageAction.execute(Unit)

            result.shouldBeSuccess()
            result.getOrThrow() shouldBe expectedUrl
        }

    @Test
    fun `execute should retry if repository call fails with RoverPhotosAreEmptyException`() =
        runTest {
            prepareSuccessfulRepositoryResponse()

            val expectedUrl = "https://marsrover.nasa.gov/image.jpg"
            coEvery {
                roverPhotosRepository.fetchAndPersistNewDailyImage(any(), any(), any())
            } returns Result.failure(RoverPhotosAreEmptyException()) andThen Result.success(
                expectedUrl
            )

            val result = refreshStoredDailyImageAction.execute(Unit)

            result.shouldBeSuccess()
            result.getOrThrow() shouldBe expectedUrl
        }

    private fun prepareSuccessfulRepositoryResponse() {
        val randomNumber = 1

        val manifest = mockk<MarsRoverMissionManifest> {
            every { roverType } returns RoverType.Curiosity
            every { maxSol } returns randomNumber
            every { photos[randomNumber] } returns mockk {
                every { totalPhotos } returns 1500
            }
        }

        val roverManifests = RoverManifests(
            curiosityManifest = manifest,
            opportunityManifest = manifest,
            spiritManifest = manifest,
        )
        coEvery { getRoverManifestsAction.execute(Unit) } returns Result.success(roverManifests)
        every { randomNumberGenerator.generate(any()) } returns randomNumber
    }
}