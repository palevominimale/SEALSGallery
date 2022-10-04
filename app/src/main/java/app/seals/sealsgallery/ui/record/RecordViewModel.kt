package app.seals.sealsgallery.ui.record

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.TrackPointDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.PolylineOptions

@SuppressLint("StaticFieldLeak")
class RecordViewModel (
    private val context: Context,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds
) : ViewModel() {

    val currentRecord = MutableLiveData<TrackDomainModel>()
    private lateinit var camera : CameraUpdate

    private val startIntent = context.getString(R.string.start_intent)
    private val stopIntent = context.getString(R.string.stop_intent)
    private val contentIntent = context.getString(R.string.track_content_intent)
    private val intentExtraName = context.getString(R.string.track_intent_name)

    companion object {
        private const val TAG = "RECORD_FRAGMENT_VM"
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == contentIntent) {
                currentRecord.postValue(intent.getSerializableExtra(intentExtraName) as TrackDomainModel)
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
        return drawTrack.invoke(currentRecord.value ?: TrackDomainModel())
    }

    fun updateCameraBounds() : CameraUpdate {
        camera = updateBounds.invoke(currentRecord.value?.trackPoints?.last() ?: TrackPointDomainModel())
        return camera
    }
}