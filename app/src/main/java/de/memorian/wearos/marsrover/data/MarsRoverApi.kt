package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.entity.MissionManifestEntity
import de.memorian.wearos.marsrover.di.ApiKey
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1"
private const val MANIFEST_URL = "$BASE_URL/manifests"

private const val FIELD_API_KEY = "api_key"

class MarsRoverApi @Inject constructor(
    private val client: HttpClient,
    @ApiKey private val apiKey: String,
) {

    suspend fun getCuriosityMissionManifest(): MissionManifestEntity {
        return client.get("$MANIFEST_URL/curiosity") {
            parameter(FIELD_API_KEY, apiKey)
        }.body()
    }
}