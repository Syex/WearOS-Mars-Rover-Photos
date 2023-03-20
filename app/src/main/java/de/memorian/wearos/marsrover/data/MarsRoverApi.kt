package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.entity.MissionManifestEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

private const val BASE_URL = "https://api.nasa.gov/mars-photos/api/v1"
private const val MANIFEST_URL = "$BASE_URL/manifests"

private const val FIELD_API_KEY = "api_key"

class MarsRoverApi(
    private val client: HttpClient,
    private val apiKey: String = "DEMO_KEY",
) {

    suspend fun getCuriosityMissionManifest(): MissionManifestEntity {
        return client.get("$MANIFEST_URL/curiosity") {
            parameter(FIELD_API_KEY, apiKey)
        }.body()
    }
}