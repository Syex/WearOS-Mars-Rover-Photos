package de.memorian.wearos.marsrover.domain.model

data class RoverManifests(
    val curiosityManifest: MarsRoverMissionManifest,
    val opportunityManifest: MarsRoverMissionManifest,
    val spiritManifest: MarsRoverMissionManifest,
)