package app.seals.sealsgallery.domain.interfaces

import app.seals.sealsgallery.data.models.UserDataModel
import app.seals.sealsgallery.data.room.UserRepositoryDAO
import app.seals.sealsgallery.domain.models.UserDomainModel

interface UserRepository : UserRepositoryDAO {
    override fun getUserById(uid: String): UserDataModel?

    fun getUserByIdDomain(uid: String): UserDomainModel?

    override fun getAll(): List<UserDataModel>?

    fun getAllDomain(): List<UserDomainModel>?

    override fun addUser(user: UserDataModel)

    fun addUser(user: UserDomainModel)

    override fun deleteUserById(uid: String)

    override fun clear()
}