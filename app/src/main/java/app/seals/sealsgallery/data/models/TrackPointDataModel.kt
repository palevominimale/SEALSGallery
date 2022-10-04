package app.seals.sealsgallery.data.models

import java.io.Serializable

data class TrackPointDataModel (
    val id: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
) : Serializable