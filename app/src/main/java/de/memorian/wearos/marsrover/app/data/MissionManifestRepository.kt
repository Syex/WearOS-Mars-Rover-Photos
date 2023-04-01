package de.memorian.wearos.marsrover.app.data

import de.memorian.wearos.marsrover.app.data.entity.MissionManifestEntity
import de.memorian.wearos.marsrover.app.data.entity.toModel
import de.memorian.wearos.marsrover.app.data.persistence.MarsRoverManifestStore
import de.memorian.wearos.marsrover.app.domain.model.MarsRoverMissionManifest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MissionManifestRepository @Inject constructor(
    private val marsRoverManifestStore: MarsRoverManifestStore,
    private val marsRoverApi: MarsRoverApi,
    private val json: Json,
) {

    suspend fun getCuriosityManifest(): MarsRoverMissionManifest {
        val manifestEntity = getStoredRoverManifest { getCuriosityManifest() }
            ?: marsRoverApi.getCuriosityMissionManifest().also {
                marsRoverManifestStore.storeCuriosityManifest(json.encodeToString(it))
            }

        return manifestEntity.toModel()
    }

    suspend fun getOpportunityManifest(): MarsRoverMissionManifest {
        val manifestEntity = getStoredRoverManifest { getOpportunityManifest() }
            ?: marsRoverApi.getOpportunityMissionManifest().also {
                marsRoverManifestStore.storeOpportunityManifest(json.encodeToString(it))
            }

        return manifestEntity.toModel()
    }

    suspend fun getSpiritManifest(): MarsRoverMissionManifest {
        val manifestEntity = getStoredRoverManifest { getSpiritManifest() }
            ?: marsRoverApi.getSpiritMissionManifest().also {
                marsRoverManifestStore.storeSpiritManifest(json.encodeToString(it))
            }

        return manifestEntity.toModel()
    }

    private suspend fun getStoredRoverManifest(
        roverFunc: suspend MarsRoverManifestStore.() -> String?,
    ): MissionManifestEntity? {
        return marsRoverManifestStore.run {
            roverFunc()?.let { json.decodeFromString<MissionManifestEntity>(it) }
        }
    }
}