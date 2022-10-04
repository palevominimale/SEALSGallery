package app.seals.sealsgallery.ui.mytracks

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.interfaces.TrackRepository
import app.seals.sealsgallery.domain.map_tools.DrawTrack
import app.seals.sealsgallery.domain.map_tools.UpdateBounds
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@SuppressLint("StaticFieldLeak")
class MyTracksViewModel (
    private val context: Context,
    private val drawTrack: DrawTrack,
    private val updateBounds: UpdateBounds,
    private val roomDB: TrackRepository
) : ViewModel() {

    val tracks = MutableLiveData<List<TrackDomainModel>>()
    private val tracksList = mutableListOf(TrackDomainModel())
    private lateinit var camera : CameraUpdate
    private val refName = context.getString(R.string.firebase_reference_name)
    private val contentIntent = context.getString(R.string.track_content_intent)
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
            }
        }
    }

    fun initReceiver() {
        val filter = IntentFilter()
        filter.addAction(contentIntent)
        filter.addAction(loggedInIntent)
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