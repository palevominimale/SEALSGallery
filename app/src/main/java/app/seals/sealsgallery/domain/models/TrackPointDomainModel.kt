package app.seals.sealsgallery.domain.models

data class TrackPointDomainModel(
    val id: Long = 0L,
    val latitude : Double = 0.0,
    val longitude : Double = 0.0,
    val altitude : Double = 0.0,
    val time : Long = 0L
)