package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.entity.RoverPhotoEntity
import de.memorian.wearos.marsrover.data.persistence.MarsRoverDailyImageStore
import de.memorian.wearos.marsrover.domain.model.RoverType
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RoverPhotosRepositoryTest {

    private val marsRoverDailyImageStore = mockk<MarsRoverDailyImageStore>()
    private val marsRoverApi = mockk<MarsRoverApi>()
    private val repository = RoverPhotosRepository(marsRoverDailyImageStore, marsRoverApi)

    @Test
    fun `getPersistedDailyImageUrl returns value from store`() = runTest {
        val expectedUrl = "http://example.com/image.jpg"
        coEvery { marsRoverDailyImageStore.getDailyImage() } returns expectedUrl

        val result = repository.getPersistedDailyImageUrl()

        result shouldBe expectedUrl
    }

    @Test
    fun `fetchAndPersistNewDailyImage returns fetched image URL and stores it in the store`() =
        runTest {
            val expectedUrl = "http://example.com/image.jpg"
            val photos = listOf(
                RoverPhotoEntity(
                    1,
                    1,
                    RoverPhotoEntity.CameraEntity(1, "Test", 1, "Test"),
                    expectedUrl,
                    LocalDate.parse("2022-03-29"),
                    RoverPhotoEntity.RoverEntity(
                        1,
                        "Test",
                        LocalDate.parse("2022-03-29"),
                        LocalDate.parse("2022-03-29"),
                        "Test"
                    )
                )
            )
            coEvery { marsRoverApi.getCuriosityPhotos(1) } returns photos
            coEvery { marsRoverDailyImageStore.storeDailyImageUrl(expectedUrl) } returns Unit

            val result = repository.fetchAndPersistNewDailyImage(RoverType.Curiosity, 1, 0)

            result.shouldBeSuccess()
            result.getOrThrow() shouldBe expectedUrl
        }

    @Test
    fun `fetchAndPersistNewDailyImage returns failure for invalid index`() = runTest {
        val result =
            repository.fetchAndPersistNewDailyImage(RoverType.Opportunity, 1, 26)

        result.shouldBeFailure()
        result.exceptionOrNull().shouldBeInstanceOf<InvalidIndexException>()
    }

    @Test
    fun `fetchAndPersistNewDailyImage returns failure for unsupported rover type`() = runTest {
        val result = repository.fetchAndPersistNewDailyImage(RoverType.Unknown, 1, 0)

        result.shouldBeFailure()
        result.exceptionOrNull().shouldBeInstanceOf<UnsupportedRoverTypeException>()
    }

    @Test
    fun `fetchAndPersistNewDailyImage returns failure for empty rover photos`() = runTest {
        coEvery { marsRoverApi.getSpiritPhotos(1) } returns emptyList()

        val result = repository.fetchAndPersistNewDailyImage(RoverType.Spirit, 1, 0)

        result.shouldBeFailure()
        result.exceptionOrNull().shouldBeInstanceOf<RoverPhotosAreEmptyException>()
    }
}
