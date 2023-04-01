package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.entity.MissionManifestEntity
import de.memorian.wearos.marsrover.data.entity.RoverPhotoEntity
import de.memorian.wearos.marsrover.data.entity.RoverPhotosEntity
import de.memorian.wearos.marsrover.di.ApiKey
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1"
private const val MANIFEST_URL = "$BASE_URL/manifests"
private const val ROVERS_URL = "$BASE_URL/rovers"

private const val PATH_CURIOSITY = "curiosity"
private const val PATH_OPPORTUNITY = "opportunity"
private const val PATH_SPIRIT = "spirit"

private const val FIELD_API_KEY = "api_key"
private const val FIELD_SOL = "sol"
private const val FIELD_CAMERA = "camera"

class MarsRoverApi @Inject constructor(
    private val client: HttpClient,
    @ApiKey private val apiKey: String,
) {

    suspend fun getCuriosityMissionManifest(): MissionManifestEntity = getMissionManifest(
        PATH_CURIOSITY
    )

    suspend fun getOpportunityMissionManifest(): MissionManifestEntity = getMissionManifest(
        PATH_OPPORTUNITY
    )

    suspend fun getSpiritMissionManifest(): MissionManifestEntity = getMissionManifest(
        PATH_SPIRIT
    )

    private suspend fun getMissionManifest(path: String): MissionManifestEntity {
        return client.get("$MANIFEST_URL/$path") {
            parameter(FIELD_API_KEY, apiKey)
        }.body()
    }

    suspend fun getCuriosityPhotos(sol: Int, camera: String? = null): List<RoverPhotoEntity> =
        getRoverPhotos(PATH_CURIOSITY, sol, camera).photos

    suspend fun getOpportunityPhotos(sol: Int, camera: String? = null): List<RoverPhotoEntity> =
        getRoverPhotos(PATH_OPPORTUNITY, sol, camera).photos

    suspend fun getSpiritPhotos(sol: Int, camera: String? = null): List<RoverPhotoEntity> =
        getRoverPhotos(PATH_SPIRIT, sol, camera).photos

    private suspend fun getRoverPhotos(
        path: String,
        sol: Int,
        camera: String?,
    ): RoverPhotosEntity {
        return client.get("$ROVERS_URL/$path/photos") {
            parameter(FIELD_API_KEY, apiKey)
            parameter(FIELD_SOL, sol)
            camera?.let { parameter(FIELD_CAMERA, it) }
        }.body()
    }
}