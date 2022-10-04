package app.seals.sealsgallery.domain.map_tools

import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.TrackPointDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class UpdateBounds {

    fun invoke(track: TrackDomainModel): CameraUpdate {
        var latA = -179.999
        var latB = 179.999
        var lonA = -179.999
        var lonB = 179.999
        track.trackPoints.forEach {
            if (it.latitude > latA) latA = it.latitude
            if (it.longitude > lonA) lonA = it.longitude
            if (it.latitude < latB) latB = it.latitude
            if (it.longitude < lonB) lonB = it.longitude
        }
        val southwest = LatLng(latB, lonB)
        val northeast = LatLng(latA, lonA)
        return CameraUpdateFactory.newLatLngBounds(LatLngBounds(southwest, northeast), 500, 500, 25)
    }

    fun invoke(point: TrackPointDomainModel) : CameraUpdate {
        val cameraPosition = CameraPosition(
            LatLng(point.latitude ,point.longitude),
            17f,
            0f,
            0f)
        return CameraUpdateFactory.newCameraPosition(cameraPosition)
    }

}