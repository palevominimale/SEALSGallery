package app.seals.sealsgallery.ui.feed.show_single

import android.view.View
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.ui.helpers.ShowPostModel
import com.google.android.gms.maps.model.PolylineOptions

class ShowFeedItemFragmentViewModel(
    private val setStartEndMarkers: SetStartEndMarkers,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
    private val showPostModel: ShowPostModel
) : ViewModel() {

    fun drawTrack(track: TrackDomainModel) = drawTrack.invoke(track)

    fun setMarkers(options: PolylineOptions) = setStartEndMarkers.invoke(options)

    fun updateCameraBounds(track: TrackDomainModel) = updateBounds.invoke(track)

    fun show(view: View, item: PostDomainModel) = showPostModel.load(view, item)
}