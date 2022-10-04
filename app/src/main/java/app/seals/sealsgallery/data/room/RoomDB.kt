package app.seals.sealsgallery.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.seals.sealsgallery.data.models.TrackDataModel


@Database(entities = [TrackDataModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDB: RoomDatabase() {

    abstract fun dao(): TrackRepositoryDAO

    companion object {
        private var INSTANCE: RoomDB? = null

        fun getInstance(context: Context): RoomDB? {
            if (INSTANCE == null) synchronized(RoomDB::class) {
                INSTANCE = Room.databaseBuilder(

                    context.applicationContext,
                    RoomDB::class.java,
                    "tracks.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}