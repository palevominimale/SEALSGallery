package app.seals.sealsgallery.domain.interfaces

import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.room.tracks.TrackRepositoryDAO
import app.seals.sealsgallery.domain.models.TrackDomainModel

interface TrackRepository : TrackRepositoryDAO {

    override fun getTrackById(id: Long) : TrackDataModel

    fun getTrackByIdForDomain(id: Long) : TrackDomainModel

    override fun getAll() : List<TrackDataModel>

    fun getAllDomain() : List<TrackDomainModel>

    override fun addTrack(track: TrackDataModel)

    fun addTrack(track: TrackDomainModel)

    override fun deleteTrackById(id: Long)
}