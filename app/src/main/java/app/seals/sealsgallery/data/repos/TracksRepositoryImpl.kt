package app.seals.sealsgallery.data.repos

import android.content.Context
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.models.TrackPointDataModel
import app.seals.sealsgallery.data.room.RoomDB
import app.seals.sealsgallery.data.room.TrackRepositoryDAO
import app.seals.sealsgallery.domain.interfaces.TrackRepository
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.TrackPointDomainModel

class TracksRepositoryImpl(context: Context) : TrackRepository {

    private val db: TrackRepositoryDAO = RoomDB.getInstance(context)?.dao()!!

    override fun getTrackById(id: Long): TrackDataModel {
        return db.getTrackById(id) ?: TrackDataModel()
    }

    override fun getTrackByIdForDomain(id: Long): TrackDomainModel {
        return getTrackById(id).mapDataToDomain()
    }

    override fun getAll(): List<TrackDataModel> {
        return db.getAll() ?: listOf(TrackDataModel())
    }

    fun getAllForDomain(): List<TrackDomainModel> {
        return getAll().mapListToDomain()
    }

    override fun addTrack(track: TrackDataModel) {
        db.addTrack(track)
    }

    override fun addTrack(track: TrackDomainModel) {
        db.addTrack(track.mapDomainToData())
    }

    override fun getAllDomain(): List<TrackDomainModel> {
        return db.getAll()?.mapListToDomain() ?: listOf(TrackDomainModel())
    }

    override fun deleteTrackById(id: Long) {
        db.deleteTrackById(id)
    }

    override fun getLastId() : TrackDataModel {
        return db.getLastId() ?: TrackDataModel()
    }

    override fun clear() {
        db.clear()
    }
}

private fun List<TrackDataModel>.mapListToDomain(): List<TrackDomainModel> {
    val listOfTracks: MutableList<TrackDomainModel> =
        mutableListOf<TrackDomainModel>().apply {
            repeat(this@mapListToDomain.size) { repeat ->
                val mappedTrackPoints = mutableListOf<TrackPointDomainModel>()
                repeat(this@mapListToDomain[repeat].trackPoints.size) {
                    mappedTrackPoints.add(this@mapListToDomain[repeat].trackPoints[it].mapDataToDomain())
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

private fun TrackDomainModel.mapDomainToData(): TrackDataModel {
    val mappedTrackPoint: MutableList<TrackPointDataModel> = mutableListOf<TrackPointDataModel>().apply {
        this@mapDomainToData.trackPoints.forEach {
            this.add(it.mapDomainToData())
        }
    }

    return TrackDataModel(
        startTime = this.startTime,
        endTime = this.endTime,
        color = this.color,
        trackPoints = mappedTrackPoint
    )
}

private fun TrackPointDomainModel.mapDomainToData(): TrackPointDataModel {
    return TrackPointDataModel(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        altitude = this.altitude
    )
}

private fun TrackPointDataModel.mapDataToDomain(): TrackPointDomainModel {
    return TrackPointDomainModel(
        id = this.id,
        latitude = this.latitude,
        longitude = this.longitude,
        altitude = this.altitude
    )
}

private fun TrackDataModel.mapDataToDomain(): TrackDomainModel {
    val mappedTrackPoint: MutableList<TrackPointDomainModel> = mutableListOf<TrackPointDomainModel>().apply {
        this@mapDataToDomain.trackPoints.forEach {
            this.add(it.mapDataToDomain())
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




