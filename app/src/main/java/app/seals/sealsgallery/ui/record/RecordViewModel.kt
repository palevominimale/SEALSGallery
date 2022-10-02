package app.seals.sealsgallery.ui.record

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

@SuppressLint("StaticFieldLeak")
class RecordViewModel (
    private val context: Context
) : ViewModel() {

    val currentRecord = MutableLiveData<TrackDomainModel>()
    private lateinit var camera : CameraUpdate

    private val startIntent = context.getString(R.string.start_intent)
    private val stopIntent = context.getString(R.string.stop_intent)
    private val contentIntent = context.getString(R.string.track_content_intent)

    companion object {
        private const val TAG = "RECORD_FRAGMENT_VM"
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == contentIntent) {
                currentRecord.postValue(intent.getSerializableExtra("track") as TrackDomainModel)
            }
            Log.e(TAG, "$intent")
        }
    }

    fun initReceiver() {
        val filter = IntentFilter()
        filter.addAction(startIntent)
        filter.addAction(stopIntent)
        filter.addAction(contentIntent)
        context.registerReceiver(receiver, filter)
    }

    fun drawTrack() : PolylineOptions {
        return PolylineOptions().apply {
            currentRecord.value?.trackPoints?.forEach {
                add(LatLng(it.latitude, it.longitude))
            }
            color(currentRecord.value?.color ?: Color.RED)
            width(15F)
            geodesic(true)
        }
    }

    fun updateCameraBounds() : CameraUpdate {
        val cameraPosition = CameraPosition(LatLng(currentRecord.value?.trackPoints?.last()?.latitude ?: 0.0
            ,currentRecord.value?.trackPoints?.last()?.longitude ?: 0.0),
        16.7f,
        0f,
        0f)
        val cam = CameraUpdateFactory.newCameraPosition(cameraPosition)
        camera = cam
        return cam
    }
}