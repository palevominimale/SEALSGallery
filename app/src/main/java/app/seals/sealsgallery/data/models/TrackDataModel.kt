package app.seals.sealsgallery.data.models

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Tracks")
data class TrackDataModel (
    @PrimaryKey                         val startTime: Long = 0L,
    @ColumnInfo(name = "endTime")       val endTime: Long = 0L,
    @ColumnInfo(name = "color")         val color: Int = Color.BLUE,
    @ColumnInfo(name = "trackPoints")   val trackPoints: MutableList<TrackPointDataModel> = mutableListOf(
        TrackPointDataModel()
    ),
    @ColumnInfo(name = "photos")        val photos: List<String> = listOf("")
) : Serializable