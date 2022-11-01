package app.seals.sealsgallery.ui.user_feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.interfaces.FeedRepository
import app.seals.sealsgallery.domain.models.PostDomainModel
import com.google.firebase.auth.FirebaseAuth

class UserFeedViewModel(
    context: Context,
    private val feedRepository: FeedRepository
) : ViewModel() {

    val userFeed = MutableLiveData<List<PostDomainModel>>()
    private val userFeedInternal = mutableListOf<PostDomainModel>()
    val auth = FirebaseAuth.getInstance()

    fun loadFeed() {
        userFeedInternal.clear()
        feedRepository.getAllDomain()?.forEach {
            if(it.user.uid == auth.currentUser?.uid) {
                userFeedInternal.add(it)
            }
        }
        userFeedInternal.sortByDescending {
            it.track.startTime
        }
        userFeed.postValue(userFeedInternal)
    }

}