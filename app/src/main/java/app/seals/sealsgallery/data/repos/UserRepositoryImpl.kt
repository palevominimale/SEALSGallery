package app.seals.sealsgallery.data.repos

import android.content.Context
import app.seals.sealsgallery.data.models.UserDataModel
import app.seals.sealsgallery.data.room.UserRepositoryDAO
import app.seals.sealsgallery.data.room.UserRoomDB
import app.seals.sealsgallery.domain.interfaces.UserRepository
import app.seals.sealsgallery.domain.models.UserDomainModel

class UserRepositoryImpl(context: Context): UserRepository {

    private val db: UserRepositoryDAO = UserRoomDB.getInstance(context)?.dao()!!

    override fun getUserById(uid: String): UserDataModel? {
        return db.getUserById(uid)
    }

    override fun getUserByIdDomain(uid: String): UserDomainModel {
        return db.getUserById(uid)?.mapToData() ?: UserDomainModel()
    }

    override fun getAll(): List<UserDataModel>? {
        return db.getAll()
    }

    override fun getAllDomain(): List<UserDomainModel> {
        return db.getAll()?.mapToDomain() ?: listOf(UserDomainModel())
    }

    override fun addUser(user: UserDomainModel) {
        db.addUser(user.mapToData())
    }

    override fun addUser(user: UserDataModel) {
        db.addUser(user)
    }

    override fun deleteUserById(uid: String) {
        db.deleteUserById(uid)
    }

    override fun clear() {
        db.clear()
    }
}

private fun List<UserDataModel>.mapToDomain(): List<UserDomainModel> {
    val res = mutableListOf<UserDomainModel>()
    this.forEach {
        res.add(UserDomainModel(
            uid = it.uid,
            name = it.name,
            photoLink = it.photoLink,
            lastLogin = it.lastLogin,
            firstLogin = it.firstLogin,
            numberOfTracks = it.numberOfTracks,
            listOfSubscribers = it.listOfSubscribers,
            listOfSubscriptions = it.listOfSubscriptions,
            bio = it.bio,
            country = it.country,
            dateOfBirth = it.dateOfBirth,
            maxRange = it.maxRange,
            avgRange = it.avgRange,
            totalTime = it.totalTime
        ))
    }
    return res
}

private fun UserDomainModel.mapToData() : UserDataModel {
    return UserDataModel(
        uid = this.uid,
        name = this.name,
        photoLink = this.photoLink,
        lastLogin = this.lastLogin,
        firstLogin = this.firstLogin,
        numberOfTracks = this.numberOfTracks,
        listOfSubscribers = this.listOfSubscribers,
        listOfSubscriptions = this.listOfSubscriptions,
        bio = this.bio,
        country = this.country,
        dateOfBirth = this.dateOfBirth,
        maxRange = this.maxRange,
        avgRange = this.avgRange,
        totalTime = this.totalTime
    )
}

private fun UserDataModel.mapToData() : UserDomainModel {
    return UserDomainModel(
        uid = this.uid,
        name = this.name,
        photoLink = this.photoLink,
        lastLogin = this.lastLogin,
        firstLogin = this.firstLogin,
        numberOfTracks = this.numberOfTracks,
        listOfSubscribers = this.listOfSubscribers,
        listOfSubscriptions = this.listOfSubscriptions,
        bio = this.bio,
        country = this.country,
        dateOfBirth = this.dateOfBirth,
        maxRange = this.maxRange,
        avgRange = this.avgRange,
        totalTime = this.totalTime
    )
}
