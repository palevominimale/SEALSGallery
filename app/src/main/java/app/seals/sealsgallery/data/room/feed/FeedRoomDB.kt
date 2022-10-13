package app.seals.sealsgallery.data.room.feed

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.models.UserDataModel
import app.seals.sealsgallery.data.room.Converters

@Database(entities = [PostDataModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class FeedRoomDB: RoomDatabase() {

    abstract fun dao(): FeedRepositoryDAO

    companion object {
        private var INSTANCE: FeedRoomDB? = null
        fun getInstance(context: Context): FeedRoomDB? {
            if (INSTANCE == null) synchronized(FeedRoomDB::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    FeedRoomDB::class.java,
                    "feed.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}