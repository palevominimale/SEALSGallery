package app.seals.sealsgallery.data.room.feed

import androidx.room.*
import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.models.UserDataModel

@Dao
interface FeedRepositoryDAO {

    @Query("SELECT * FROM User")
    fun getAll() : List<PostDataModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPost(post: PostDataModel)

    @Query("DELETE FROM User")
    fun clear()
}