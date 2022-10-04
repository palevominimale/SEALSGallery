package app.seals.sealsgallery.ui.mytracks

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyTracksViewModel (context: Context) : ViewModel() {

    val tracks = MutableLiveData<List<TrackDomainModel>>()
    private val tracksList = mutableListOf(TrackDomainModel())
    private lateinit var camera : CameraUpdate
    private val refName = context.getString(R.string.firebase_reference_name)

    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val ref = db.getReference(refName).child(auth.currentUser?.uid.toString())

    fun loadTracks() {
        ref.get().addOnCompleteListener { snapshot ->
            tracksList.clear()
            snapshot.result.children.forEach { children ->
                try {
                    tracksList.add(
                        children.getValue(TrackDomainModel::class.java) ?: TrackDomainModel()
                    )
                    tracks.postValue(tracksList)
                } catch (e: Exception) {
                    Log.e("MTVM", "$e")
                    e.printStackTrace()
                }
            }
        }
    }

    fun drawTrack(track: TrackDomainModel) : PolylineOptions {
        return PolylineOptions().apply {
            track.trackPoints.forEach {
                add(LatLng(it.latitude, it.longitude))
            }
            color(track.color)
            width(15F)
            geodesic(true)
        }
    }

    fun updateCameraBounds(track: TrackDomainModel) : CameraUpdate {
        var latA = -179.999
        var latB = 179.999
        var lonA = -179.999
        var lonB = 179.999
        track.trackPoints.forEach {
            if(it.latitude > latA) latA = it.latitude
            if(it.longitude > lonA) lonA = it.longitude
            if(it.latitude < latB) latB = it.latitude
            if(it.longitude < lonB) lonB = it.longitude
        }
        val southwest = LatLng(latB, lonB)
        val northeast = LatLng(latA, lonA)
        val cam = CameraUpdateFactory.newLatLngBounds(LatLngBounds(southwest,northeast), 500,500,25)
        camera = cam
        return cam
    }

}