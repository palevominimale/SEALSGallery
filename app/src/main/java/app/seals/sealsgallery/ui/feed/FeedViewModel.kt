package app.seals.sealsgallery.ui.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.PolylineOptions

class FeedViewModel(

    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
) : ViewModel() {

    val feed = MutableLiveData<List<PostDomainModel>>()
    val feedList = mutableListOf<PostDomainModel>()

    fun drawTrack(track: TrackDomainModel) : PolylineOptions {
        return drawTrack.invoke(track)
    }

    fun updateCameraBounds(track: TrackDomainModel) : CameraUpdate {
        return updateBounds.invoke(track)
    }

    fun loadFeed() {

    }

    fun generateFeed() {
        repeat(10) {
            feedList.add(PostDomainModel())
        }
        feed.postValue(feedList)
    }
}