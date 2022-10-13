package app.seals.sealsgallery.data.models

import java.io.Serializable

data class PostDataModel(
    val user: UserDataModel = UserDataModel(),
    val track: TrackDataModel = TrackDataModel()
) : Serializable