package app.seals.sealsgallery.domain.models

import java.io.Serializable

data class PostDomainModel(
    val user: UserDomainModel = UserDomainModel(),
    val track: TrackDomainModel = TrackDomainModel(),
    val id: Long = track.startTime
) : Serializable