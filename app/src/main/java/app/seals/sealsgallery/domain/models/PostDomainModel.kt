package app.seals.sealsgallery.domain.models

data class PostDomainModel(
    val user: UserDomainModel = UserDomainModel(),
    val track: TrackDomainModel = TrackDomainModel()
) {
}