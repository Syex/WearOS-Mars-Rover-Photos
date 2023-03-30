package de.memorian.wearos.marsrover.data.entity

import de.memorian.wearos.marsrover.domain.model.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A mission manifest for a rover contains general information about the photos that a rover
 * has taken.
 */
@Serializable
data class MissionManifestEntity(
    @SerialName("photo_manifest") val photoManifest: PhotoManifestEntity,
) {

    @Serializable
    data class PhotoManifestEntity(
        val name: String,
        @SerialName("landing_date") val landingDate: LocalDate,
        @SerialName("launch_date") val launchDate: LocalDate,
        val status: String,
        @SerialName("max_sol") val maxSol: Int,
        @SerialName("max_date") val maxEarthDate: LocalDate,
        @SerialName("total_photos") val totalPhotos: Int,
        val photos: List<PhotosPerSolEntity>,
    ) {

        @Serializable
        data class PhotosPerSolEntity(
            val sol: Int,
            @SerialName("earth_date") val earthDate: LocalDate,
            @SerialName("total_photos") val totalPhotos: Int,
            val cameras: List<String>,
        )
    }
}

fun MissionManifestEntity.toModel() = MarsRoverMissionManifest(
    roverName = photoManifest.name,
    roverType = RoverType.fromRoverName(photoManifest.name),
    landingDate = photoManifest.landingDate,
    launchDate = photoManifest.launchDate,
    status = RoverStatus.fromJson(photoManifest.status),
    maxSol = photoManifest.maxSol,
    maxEarthDate = photoManifest.maxEarthDate,
    totalPhotos = photoManifest.totalPhotos,
    photos = photoManifest.photos.map { photosPerSolEntity ->
        PhotosPerSol(
            sol = photosPerSolEntity.sol,
            earthDate = photosPerSolEntity.earthDate,
            totalPhotos = photosPerSolEntity.totalPhotos,
            cameras = photosPerSolEntity.cameras.mapNotNull { RoverCamera.fromJson(it) }
        )
    }
)
