package app.seals.sealsgallery.data.room

import androidx.room.*
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.models.UserDataModel

@Dao
interface UserRepositoryDAO {

    @Query("SELECT * FROM User WHERE uid LIKE :uid LIMIT 1")
    fun getUserById(uid: String) : UserDataModel?

    @Query("SELECT * FROM User")
    fun getAll() : List<UserDataModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: UserDataModel)

    @Query("DELETE FROM User WHERE uid LIKE :uid")
    fun deleteUserById(uid: String)

    @Query("DELETE FROM User")
    fun clear()
}