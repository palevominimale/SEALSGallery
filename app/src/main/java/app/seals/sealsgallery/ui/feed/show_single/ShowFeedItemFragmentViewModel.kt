package app.seals.sealsgallery.ui.feed.show_single

import android.content.Context
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.model.PolylineOptions

class ShowFeedItemFragmentViewModel(
    context: Context,
    private val setStartEndMarkers: SetStartEndMarkers,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
) : ViewModel() {

    fun drawTrack(track: TrackDomainModel) = drawTrack.invoke(track)

    fun setMarkers(options: PolylineOptions) = setStartEndMarkers.invoke(options)

    fun updateCameraBounds(track: TrackDomainModel) = updateBounds.invoke(track)

}