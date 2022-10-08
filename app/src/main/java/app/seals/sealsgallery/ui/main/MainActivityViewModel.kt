package app.seals.sealsgallery.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.interfaces.UserRepository
import app.seals.sealsgallery.domain.models.UserDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivityViewModel(
    private val userRepository: UserRepository,
    context: Context
) : ViewModel() {

    private val refMainNode = context.getString(R.string.firebase_reference_name)
    private val refUserDataNode = context.getString(R.string.firebase_reference_user_data_name)
    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid.toString()
    private val ref = db.getReference(refMainNode).child(uid).child(refUserDataNode)

    fun getUser(uid: String) {
        userRepository.getUserById(uid)
    }

    fun setUser(user: UserDomainModel) {
        userRepository.addUser(user)
        ref.setValue(user)
    }

}