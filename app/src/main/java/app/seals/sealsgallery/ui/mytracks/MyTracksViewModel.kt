package app.seals.sealsgallery.ui.mytracks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase

class MyTracksViewModel : ViewModel() {

    val tracks = MutableLiveData<List<TrackDomainModel>>()
    private val tracksList = mutableListOf<TrackDomainModel>()

    companion object {
        private val db = FirebaseDatabase.getInstance()
        private val auth = FirebaseAuth.getInstance()
        private val ref = db.getReference("tracks").child(auth.currentUser?.uid.toString())
    }

    fun loadTracks() {
        ref.get().addOnCompleteListener { snapshot ->
            snapshot.result.children.forEach { children ->
                tracksList.add(children.getValue(TrackDomainModel::class.java) ?: TrackDomainModel())
                Log.e("MYTRACKS_VM", "${children.getValue(TrackDomainModel::class.java)}")
                Log.e("MYTRACKS_VM", "${children.value}")
                tracks.postValue(tracksList)
            }
        }
    }

}