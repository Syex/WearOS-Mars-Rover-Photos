package de.memorian.wearos.marsrover.data

import de.memorian.wearos.marsrover.data.persistence.MarsRoverManifestStore
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MissionManifestRepositoryTest {

    private val marsRoverManifestStore = mockk<MarsRoverManifestStore>(relaxUnitFun = true)
    private val api = mockk<MarsRoverApi>()
    private val json = Json

    private val testJson = """
        {
          "photo_manifest": {
            "name": "Curiosity",
            "landing_date": "2012-08-06",
            "launch_date": "2011-11-26",
            "status": "active",
            "max_sol": 3782,
            "max_date": "2023-03-27",
            "total_photos": 641115,
            "photos": [
              {
                "sol": 0,
                "earth_date": "2012-08-06",
                "total_photos": 3702,
                "cameras": [
                  "CHEMCAM",
                  "FHAZ",
                  "MARDI",
                  "RHAZ"
                ]
              }
            ]
          }
        }
    """.trimIndent()

    private val repository = MissionManifestRepository(marsRoverManifestStore, api, json)

    @Test
    fun `returns stored curiosity manifest from store`() = runTest {
        coEvery { marsRoverManifestStore.getCuriosityManifest() } returns testJson

        val roverManifest = repository.getCuriosityManifest()
        roverManifest.roverName shouldBe "Curiosity"

        coVerify(exactly = 0) { api.getCuriosityMissionManifest() }
        coVerify(exactly = 0) { marsRoverManifestStore.storeCuriosityManifest(any()) }
    }

    @Test
    fun `calls api and stores result in store if no manifest is available in store`() = runTest {
        coEvery { marsRoverManifestStore.getCuriosityManifest() } returns null
        coEvery { api.getCuriosityMissionManifest() } returns json.decodeFromString(testJson)

        val roverManifest = repository.getCuriosityManifest()
        roverManifest.roverName shouldBe "Curiosity"

        coVerify { api.getCuriosityMissionManifest() }
        coVerify { marsRoverManifestStore.storeCuriosityManifest(any()) }
    }
}