package de.memorian.wearos.marsrover.data.entity

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoverPhotoEntity(
    val id: Int,
    val sol: Int,
    val camera: CameraEntity,
    @SerialName("img_src") val imgSrc: String,
    @SerialName("earth_date") val earthDate: LocalDate,
    val rover: RoverEntity,
) {

    @Serializable
    data class CameraEntity(
        val id: Int,
        val name: String,
        @SerialName("rover_id") val roverId: Int,
        @SerialName("full_name") val fullName: String,
    )

    @Serializable
    data class RoverEntity(
        val id: Int,
        val name: String,
        @SerialName("landing_date") val landingDate: LocalDate,
        @SerialName("launch_date") val launchDate: LocalDate,
        val status: String,
    )
}