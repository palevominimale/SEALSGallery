package app.seals.sealsgallery.ui.record

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class RecordViewModel : ViewModel() {

    val currentRecord = MutableLiveData<TrackDomainModel>()
    private lateinit var camera : CameraUpdate

    companion object {
        private val db = FirebaseDatabase.getInstance()
        private val auth = FirebaseAuth.getInstance()
        private val ref = db.getReference("tracks").child(auth.currentUser?.uid.toString())
        private val scope = CoroutineScope(Dispatchers.IO)
        private const val TAG = "RECORD_FRAGMENT"
    }

    private val listener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val track = snapshot.getValue(TrackDomainModel::class.java) ?: TrackDomainModel()
            Log.e(TAG, "$track")
            currentRecord.postValue(track)
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    fun setObservedTrack() {
        ref.get().addOnCompleteListener {
            val material = it.result.children.last().key.toString()
            ref.child(material).addValueEventListener(listener)
        }
    }

    fun drawTrack(track: TrackDomainModel) : PolylineOptions {
        return PolylineOptions().apply {
            track.trackPoints.forEach {
                add(LatLng(it.latitude, it.longitude))
            }
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