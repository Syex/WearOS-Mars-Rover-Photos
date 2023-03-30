package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.persistence.MarsRoverDailyImageStore
import de.memorian.wearos.marsrover.domain.model.MarsRoverImageUrl
import de.memorian.wearos.marsrover.domain.model.RoverType
import javax.inject.Inject

const val PHOTOS_PER_PAGE = 25

class RoverPhotosRepository @Inject constructor(
    private val marsRoverDailyImageStore: MarsRoverDailyImageStore,
    private val marsRoverApi: MarsRoverApi,
) {

    suspend fun getPersistedDailyImageUrl(): String? = marsRoverDailyImageStore.getDailyImage()

    suspend fun fetchAndPersistNewDailyImage(
        roverType: RoverType,
        sol: Int,
        index: Int,
    ): Result<MarsRoverImageUrl> {
        if (index > PHOTOS_PER_PAGE - 1) return Result.failure(InvalidIndexException())

        val roverPhotos = when (roverType) {
            RoverType.Curiosity -> marsRoverApi.getCuriosityPhotos(sol)
            RoverType.Opportunity -> marsRoverApi.getOpportunityPhotos(sol)
            RoverType.Spirit -> marsRoverApi.getSpiritPhotos(sol)
            RoverType.Unknown -> return Result.failure(UnsupportedRoverTypeException())
        }

        if (roverPhotos.isEmpty()) return Result.failure(RoverPhotosAreEmptyException())

        val realIndex = if (index <= roverPhotos.lastIndex) index else roverPhotos.lastIndex
        val imageUrl = roverPhotos[realIndex].imgSrc

        marsRoverDailyImageStore.storeDailyImageUrl(imageUrl)

        return Result.success(imageUrl)
    }
}

class InvalidIndexException : Exception(
    "Paging is not yet supported, can only select from first page (25 images max)"
)

class UnsupportedRoverTypeException : Exception("Cannot fetch image for unknown rover type")

class RoverPhotosAreEmptyException : Exception()