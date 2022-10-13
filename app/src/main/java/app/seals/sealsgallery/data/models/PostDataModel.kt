package app.seals.sealsgallery.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Feed")
data class PostDataModel(
    @ColumnInfo(name = "user")      val user: UserDataModel = UserDataModel(),
    @ColumnInfo(name = "track")     val track: TrackDataModel = TrackDataModel(),
    @PrimaryKey                     val id: Long = track.startTime,
) : Serializable