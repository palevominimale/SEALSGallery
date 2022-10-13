package app.seals.sealsgallery.data.repos

import android.content.Context
import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.room.feed.FeedRepositoryDAO
import app.seals.sealsgallery.data.room.feed.FeedRoomDB
import app.seals.sealsgallery.domain.interfaces.FeedRepository
import app.seals.sealsgallery.domain.models.PostDomainModel

class UserRepositoryImpl (context: Context): FeedRepository {

    private val db: FeedRepositoryDAO = FeedRoomDB.getInstance(context)?.dao()!!

    override fun getAll(): List<PostDataModel>? {
        return db.getAll()
    }

    override fun getAllDomain(): List<PostDomainModel> {
        return db.getAll()?.mapToDomain() ?: listOf(PostDomainModel())
    }

    override fun addPost(post: PostDataModel) {
        db.addPost(post)
    }

    override fun addPost(post: PostDomainModel) {
        db.addPost(post.mapToDomain())
    }

    override fun clear() {
        db.clear()
    }
}