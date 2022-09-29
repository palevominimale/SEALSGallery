package app.seals.sealsgallery.domain.models

import android.graphics.Color

data class TrackDomainModel (
        var startTime: Long = 0L,
        var endTime: Long = 0L,
        var color: Int = Color.BLUE,
        val trackPoints: MutableList<TrackPointDomainModel> = mutableListOf(TrackPointDomainModel()),
        var photos: List<String> = listOf("")
)