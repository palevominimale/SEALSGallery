package app.seals.sealsgallery.ui.main

import androidx.lifecycle.ViewModel
import app.seals.sealsgallery.domain.interfaces.UserRepository
import app.seals.sealsgallery.domain.models.UserDomainModel

class MainActivityViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getUser(uid: String) {
        userRepository.getUserById(uid)
    }

    fun setUser(user: UserDomainModel) {
        userRepository.addUser(user)
    }

}