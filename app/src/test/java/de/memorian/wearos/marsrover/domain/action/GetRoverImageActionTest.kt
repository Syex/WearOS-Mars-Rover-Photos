package de.memorian.wearos.marsrover.domain.action

import de.memorian.wearos.marsrover.data.RoverPhotosRepository
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRoverImageActionTest {

    private val roverPhotosRepository = mockk<RoverPhotosRepository>()
    private val refreshStoredDailyImageAction = mockk<RefreshStoredDailyImageAction>()

    private val action = GetRoverImageAction(
        roverPhotosRepository,
        refreshStoredDailyImageAction
    )

    @Test
    fun `execute should return persisted image from repository if available`() = runTest {
        val expectedUrl = "https://marsrover.nasa.gov/image.jpg"
        coEvery { roverPhotosRepository.getPersistedDailyImageUrl() } returns expectedUrl

        val result = action.execute(Unit)

        result.shouldBeSuccess()
        result.getOrThrow() shouldBe expectedUrl
    }

    @Test
    fun `execute should refresh daily image if there is no persisted image available`() = runTest {
        val expectedUrl = "https://marsrover.nasa.gov/image.jpg"
        coEvery { roverPhotosRepository.getPersistedDailyImageUrl() } returns null
        coEvery {
            refreshStoredDailyImageAction.execute(Unit)
        } returns Result.success(expectedUrl)

        val result = action.execute(Unit)

        result.shouldBeSuccess()
        result.getOrThrow() shouldBe expectedUrl
    }
}