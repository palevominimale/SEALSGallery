package app.seals.sealsgallery.data.repos

import android.content.Context
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.room.tracks.TracksRoomDB
import app.seals.sealsgallery.data.room.tracks.TrackRepositoryDAO
import app.seals.sealsgallery.domain.interfaces.TrackRepository
import app.seals.sealsgallery.domain.models.TrackDomainModel

class TracksRepositoryImpl(context: Context) : TrackRepository {

    private val db: TrackRepositoryDAO = TracksRoomDB.getInstance(context)?.dao()!!

    override fun getTrackById(id: Long): TrackDataModel {
        return db.getTrackById(id) ?: TrackDataModel()
    }

    override fun getTrackByIdForDomain(id: Long): TrackDomainModel {
        return getTrackById(id).mapToDomain()
    }

    override fun getAll(): List<TrackDataModel> {
        return db.getAll() ?: listOf(TrackDataModel())
    }

    override fun addTrack(track: TrackDataModel) {
        db.addTrack(track)
    }

    override fun addTrack(track: TrackDomainModel) {
        db.addTrack(track.mapToData())
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



