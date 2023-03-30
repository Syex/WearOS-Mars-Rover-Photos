package de.memorian.wearos.marsrover.domain.model

import kotlinx.datetime.LocalDate

data class MarsRoverMissionManifest(
    val roverName: String,
    val landingDate: LocalDate,
    val launchDate: LocalDate,
    val status: RoverStatus,
    val maxSol: Int,
    val maxEarthDate: LocalDate,
    val totalPhotos: Int,
    val photos: List<PhotosPerSol>,
)

data class PhotosPerSol(
    val sol: Int,
    val earthDate: LocalDate,
    val totalPhotos: Int,
    val cameras: List<RoverCamera>,
)

enum class RoverStatus {

    ACTIVE,
    INACTIVE;

    companion object {

        fun fromJson(json: String): RoverStatus = when (json) {
            "active" -> ACTIVE
            else -> INACTIVE
        }
    }
}

enum class RoverCamera {

    FrontHazardAvoidanceCamera,
    RearHazardAvoidanceCamera,
    MastCamera,
    ChemistryAndCameraComplex,
    MarsHandLensImager,
    MarsDescentImager,
    NavigationCamera,
    PanoramicCamera,
    MiniatureThermalEmissionSpectrometer;

    companion object {

        fun fromJson(json: String): RoverCamera? = when (json) {
            "FHAZ" -> FrontHazardAvoidanceCamera
            "RHAZ" -> RearHazardAvoidanceCamera
            "MAST" -> MastCamera
            "CHEMCAM" -> ChemistryAndCameraComplex
            "MAHLI" -> MarsHandLensImager
            "MARDI" -> MarsDescentImager
            "NAVCAM" -> NavigationCamera
            "PANCAM" -> PanoramicCamera
            "MINITES" -> MiniatureThermalEmissionSpectrometer
            else -> null
        }
    }
}

/**
 * @Serializable
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
 */