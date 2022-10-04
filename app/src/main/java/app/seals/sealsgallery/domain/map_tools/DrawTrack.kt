package app.seals.sealsgallery.domain.map_tools

import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap

class DrawTrack {

    fun invoke(track: TrackDomainModel) : PolylineOptions {
        return PolylineOptions().apply {
            track.trackPoints.forEach {
                add(LatLng(it.latitude, it.longitude))
            }
            val cap = RoundCap()
            color(track.color)
            width(15F)
            geodesic(true)
            jointType(JointType.ROUND)
            startCap(cap)
            endCap(cap)
        }
    }
}