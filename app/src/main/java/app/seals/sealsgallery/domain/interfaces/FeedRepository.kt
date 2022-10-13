package app.seals.sealsgallery.domain.interfaces

import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.models.UserDataModel
import app.seals.sealsgallery.data.room.feed.FeedRepositoryDAO
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.UserDomainModel

interface FeedRepository : FeedRepositoryDAO {

    override fun getAll(): List<PostDataModel>?

    fun getAllDomain(): List<PostDomainModel>?

    override fun addPost(post: PostDataModel)

    fun addPost(post: PostDomainModel)

    fun addAll(feed: List<PostDomainModel>)

    override fun clear()
}