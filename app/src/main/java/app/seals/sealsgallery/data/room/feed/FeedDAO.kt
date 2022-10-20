package app.seals.sealsgallery.data.room.feed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.seals.sealsgallery.data.models.PostDataModel

@Dao
interface FeedRepositoryDAO {

    @Query("SELECT * FROM Feed")
    fun getAll() : List<PostDataModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPost(post: PostDataModel)

    @Query("DELETE FROM Feed")
    fun clear()
}