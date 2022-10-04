package app.seals.sealsgallery.domain.map_tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.maps.model.*

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