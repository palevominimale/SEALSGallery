package app.seals.sealsgallery.data.repos

import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.models.TrackPointDataModel
import app.seals.sealsgallery.data.models.UserDataModel
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.TrackPointDomainModel
import app.seals.sealsgallery.domain.models.UserDomainModel

fun PostDataModel.mapToDomain(): PostDomainModel {
    return PostDomainModel(
        user = this.user.mapToDomain(),
        track = this.track.mapToDomain()
    )
}

fun PostDomainModel.mapToDomain(): PostDataModel {
    return PostDataModel(
        user = this.user.mapToData(),
        track = this.track.mapToData()
    )
}

fun List<PostDataModel>.mapToDomain(): List<PostDomainModel> {
    val res = mutableListOf<PostDomainModel>()
    this.forEach {
        res.add(it.mapToDomain())
    }
    return res
}

fun UserDomainModel.mapToData() : UserDataModel {
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

fun UserDataModel.mapToDomain() : UserDomainModel {
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

fun List<TrackDataModel>.mapListToDomain(): List<TrackDomainModel> {
    val listOfTracks: MutableList<TrackDomainModel> =
        mutableListOf<TrackDomainModel>().apply {
            repeat(this@mapListToDomain.size) { repeat ->
                val mappedTrackPoints = mutableListOf<TrackPointDomainModel>()
                repeat(this@mapListToDomain[repeat].trackPoints.size) {
                    mappedTrackPoints.add(this@mapListToDomain[repeat].trackPoints[it].mapToDomain())
                }
                add(TrackDomainModel(
                    startTime = this@mapListToDomain[repeat].startTime,
                    endTime = this@mapListToDomain[repeat].endTime,
                    color = this@mapListToDomain[repeat].color,
                    trackPoints = mappedTrackPoints
                )
                )
            }
        }
    return listOfTracks
}

fun TrackDomainModel.mapToData(): TrackDataModel {
    val mappedTrackPoint: MutableList<TrackPointDataModel> = mutableListOf<TrackPointDataModel>().apply {
        this@mapToData.trackPoints.forEach {
            this.add(it.mapToData())
        }
    }

    return TrackDataModel(
        startTime = this.startTime,
        endTime = this.endTime,
        color = this.color,
        trackPoints = mappedTrackPoint
    )
}

fun TrackPointDomainModel.mapToData(): TrackPointDataModel {
    return TrackPointDataModel(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        altitude = this.altitude
    )
}

fun TrackPointDataModel.mapToDomain(): TrackPointDomainModel {
    return TrackPointDomainModel(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        altitude = this.altitude
    )
}

fun TrackDataModel.mapToDomain(): TrackDomainModel {
    val mappedTrackPoint: MutableList<TrackPointDomainModel> = mutableListOf<TrackPointDomainModel>().apply {
        this@mapToDomain.trackPoints.forEach {
            this.add(it.mapToDomain())
        }
    }
    return TrackDomainModel(
        startTime = this.startTime,
        endTime = this.endTime,
        color = this.color,
        trackPoints = mappedTrackPoint,
        photos = this.photos
    )
}


