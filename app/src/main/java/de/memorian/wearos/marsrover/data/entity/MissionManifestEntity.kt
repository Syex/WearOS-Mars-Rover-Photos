package de.memorian.wearos.marsrover.data.entity

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
        val photos: List<PhotosPerSol>,
    ) {

        @Serializable
        data class PhotosPerSol(
            val sol: Int,
            @SerialName("earth_date") val earthDate: LocalDate,
            @SerialName("total_photos") val totalPhotos: Int,
            val cameras: List<String>,
        )
    }
}

