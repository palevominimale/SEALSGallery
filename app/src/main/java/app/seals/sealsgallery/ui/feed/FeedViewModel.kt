package app.seals.sealsgallery.ui.feed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.interfaces.FeedRepository
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.UserDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel(
    context: Context,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
    private val feedRepository: FeedRepository
) : ViewModel() {

    val feed = MutableLiveData<List<PostDomainModel>>()
    private val feedList = mutableListOf<PostDomainModel>()
    private val refMainNode = context.getString(R.string.firebase_reference_name)
    private val refTracksNode = context.getString(R.string.firebase_reference_tracks_name)
    private val refUserDataNode = context.getString(R.string.firebase_reference_user_data)
    private val db = FirebaseDatabase.getInstance()
    private val ref = db.getReference(refMainNode)

    fun drawTrack(track: TrackDomainModel) : PolylineOptions {
        return drawTrack.invoke(track)
    }

    fun updateCameraBounds(track: TrackDomainModel) : CameraUpdate {
        return updateBounds.invoke(track)
    }

    fun loadFeed() {
        feedList.clear()
        feedList.addAll(feedRepository.getAllDomain() ?: listOf())
        feedList.sortByDescending {
            it.track.startTime
        }
        feed.postValue(feedList)
    }

    fun loadFeedFromFirebase() {
        ref.get().addOnCompleteListener { snapshot ->
            feedList.clear()
            snapshot.result.children.forEach { children ->
                val user = children.child(refUserDataNode).getValue(UserDomainModel::class.java)
                children.child(refTracksNode).children.forEach {
                    feedList.add(PostDomainModel(
                        user = user ?: UserDomainModel(),
                        track = it.getValue(TrackDomainModel::class.java) ?: TrackDomainModel()
                        ))
                }
            }
            feedList.sortByDescending {
                it.track.startTime
            }
            feed.postValue(feedList)
            CoroutineScope(Dispatchers.IO).launch {
                feedRepository.clear()
                feedRepository.addAll(feedList)
            }
        }
    }
}