package app.seals.sealsgallery.ui.user_feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.interfaces.FeedRepository
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.UserDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserFeedViewModel(
    context: Context,
    private val feedRepository: FeedRepository
) : ViewModel() {

    val userFeed = MutableLiveData<List<PostDomainModel>>()
    val userData = MutableLiveData<UserDomainModel>()
    private val refMainNode = context.getString(
        R.string.firebase_reference_name)
    private val refTracksNode = context.getString(R.string.firebase_reference_tracks_name)
    private val refUserDataNode = context.getString(R.string.firebase_reference_user_data)
    private val db = FirebaseDatabase.getInstance()
    private val userFeedInternal = mutableListOf<PostDomainModel>()
    val auth = FirebaseAuth.getInstance()
    private val ref = db.getReference(refMainNode)
    private val refUserData = ref.child(auth.uid.toString()).child(refUserDataNode)

    fun loadUserData() {
        refUserData.get().addOnCompleteListener { snapshot ->
            userData.postValue(snapshot.result.getValue(UserDomainModel::class.java))
        }
    }

    fun loadUserDataByUid(uid: String) {
        val refUserById = ref.child(uid).child(refUserDataNode)
    }

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