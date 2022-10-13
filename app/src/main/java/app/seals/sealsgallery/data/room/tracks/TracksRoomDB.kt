package app.seals.sealsgallery.data.room.tracks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.room.Converters


@Database(entities = [TrackDataModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TracksRoomDB: RoomDatabase() {

    abstract fun dao(): TrackRepositoryDAO

    companion object {
        private var INSTANCE: TracksRoomDB? = null

        fun getInstance(context: Context): TracksRoomDB? {
            if (INSTANCE == null) synchronized(TracksRoomDB::class) {
                INSTANCE = Room.databaseBuilder(

                    context.applicationContext,
                    TracksRoomDB::class.java,
                    "tracks.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}