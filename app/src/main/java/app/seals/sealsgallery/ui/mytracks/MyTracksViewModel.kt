package app.seals.sealsgallery.ui.mytracks

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.images.ImagesPicker
import app.seals.sealsgallery.domain.interfaces.TrackRepository
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
class MyTracksViewModel (
    private val context: Context,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
    private val roomDB: TrackRepository,
    private val imagesPicker: ImagesPicker
) : ViewModel() {

    val tracks = MutableLiveData<List<TrackDomainModel>>()
    val currentTrackImages = MutableLiveData<List<MarkerOptions>>()
    private lateinit var camera : CameraUpdate
    private val tracksList = mutableListOf(TrackDomainModel())
    private val refName = context.getString(R.string.firebase_reference_name)
    private val contentIntent = context.getString(R.string.track_content_intent)
    private val stopIntent = context.getString(R.string.stop_intent)
    private val loggedInIntent = context.getString(R.string.logged_in_intent)
    private val intentExtraName = context.getString(R.string.track_intent_name)
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val ref = db.getReference(refName).child(auth.currentUser?.uid.toString())

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action) {
                contentIntent -> {
                    if(tracksList.size>0) tracksList.removeLast()
                    tracksList.add(intent.getSerializableExtra(intentExtraName) as TrackDomainModel)
                    tracks.postValue(tracksList)
                }
                loggedInIntent -> {
                    loadTracksFromFirebase()
                }
                stopIntent -> {
                    if(tracksList.size>0) tracksList.removeLast()
                    val track = intent.getSerializableExtra(intentExtraName) as TrackDomainModel
                    tracksList.add(track)
                    loadImages(track)
                    tracks.postValue(tracksList)
                }
            }
        }
    }

    fun loadImages(track: TrackDomainModel) {
        val markers = mutableListOf<MarkerOptions>()
        val res = imagesPicker.invoke(track)
        CoroutineScope(Dispatchers.IO).launch {
            res.forEach {
                val thumb = ThumbnailUtils.extractThumbnail(
                    MediaStore.Images.Media.getBitmap(
                        context.contentResolver,
                        it.uri
                    ),
                    150,
                    150
                )
                markers.add(MarkerOptions().apply {
                    Log.e("MTF_", "$it")
                    position(it.latLng ?: LatLng(0.0, 0.0))
                    icon(BitmapDescriptorFactory.fromBitmap(thumb))
                })
            }
        }.invokeOnCompletion {
            currentTrackImages.postValue(markers)
        }
    }

    fun initReceiver() {
        val filter = IntentFilter()
        filter.addAction(contentIntent)
        filter.addAction(loggedInIntent)
        filter.addAction(stopIntent)
        context.registerReceiver(receiver, filter)
    }

    fun loadTracksFromFirebase() {
        ref.get().addOnCompleteListener { snapshot ->
            tracksList.clear()
            roomDB.clear()
            snapshot.result.children.forEach { children ->
                try {
                    val t = children.getValue(TrackDomainModel::class.java) ?: TrackDomainModel()
                    roomDB.addTrack(t)
                    tracksList.add(t)
                    tracks.postValue(tracksList)

                } catch (e: Exception) {
                    Log.e("MTVM", "$e")
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadCachedTracks() {
        tracksList.clear()
        tracksList.addAll(roomDB.getAllDomain())
        tracks.postValue(tracksList)
    }

    fun drawTrack(track: TrackDomainModel) : PolylineOptions {
        return drawTrack.invoke(track)
    }

    fun updateCameraBounds(track: TrackDomainModel) : CameraUpdate {
        camera = updateBounds.invoke(track)
        return camera
    }

}