package app.seals.sealsgallery.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.interfaces.FeedRepository
import app.seals.sealsgallery.domain.models.UserDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivityViewModel(
    context: Context
) : ViewModel() {

    private val refMainNode = context.getString(R.string.firebase_reference_name)
    private val refUserDataNode = context.getString(R.string.firebase_reference_user_data)
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid.toString()
    private val ref = db.getReference(refMainNode).child(uid).child(refUserDataNode)

    fun setUser(user: UserDomainModel) {
        ref.setValue(user)
    }

    fun setLastActivation(time: Long) {
        ref.child("lastLogin").setValue(time)
    }

}