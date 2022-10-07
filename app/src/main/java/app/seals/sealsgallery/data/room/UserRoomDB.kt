package app.seals.sealsgallery.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.seals.sealsgallery.data.models.UserDataModel

@Database(entities = [UserDataModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class UserRoomDB: RoomDatabase() {

    abstract fun dao(): UserRepositoryDAO

    companion object {
        private var INSTANCE: UserRoomDB? = null
        fun getInstance(context: Context): UserRoomDB? {
            if (INSTANCE == null) synchronized(UserRoomDB::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDB::class.java,
                    "user.db"
                ).allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}