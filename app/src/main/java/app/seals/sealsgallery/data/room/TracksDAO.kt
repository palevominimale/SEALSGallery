package app.seals.sealsgallery.data.room

import androidx.room.*
import app.seals.sealsgallery.data.models.TrackDataModel

@Dao
interface TrackRepositoryDAO {

    @Query("SELECT * FROM Tracks WHERE startTime LIKE :id LIMIT 1")
    fun getTrackById(id: Long) : TrackDataModel?

    @Query("SELECT * FROM Tracks")
    fun getAll() : List<TrackDataModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTrack(track: TrackDataModel)

    @Query("DELETE FROM Tracks WHERE startTime LIKE :id")
    fun deleteTrackById(id: Long)

    @Query("DELETE FROM Tracks")
    fun clear()

    @Query("SELECT * FROM Tracks ORDER BY startTime DESC LIMIT 1 ")
    fun getLastId() : TrackDataModel?
}