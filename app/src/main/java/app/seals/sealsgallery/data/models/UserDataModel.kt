package app.seals.sealsgallery.data.models

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class UserDataModel(
    @PrimaryKey                                 var uid: String = "",
    @ColumnInfo(name = "name")                  var name: String = "",
    @ColumnInfo(name = "photoLink")             var photoLink: String = "",
    @ColumnInfo(name = "lastLogin")             var lastLogin: Long = 0,
    @ColumnInfo(name = "firstLogin")            var firstLogin: Long = 0,
    @ColumnInfo(name = "numberOfTracks")        var numberOfTracks: Int = 0,
    @ColumnInfo(name = "listOfSubscribers")     var listOfSubscribers: List<String> = listOf(""),
    @ColumnInfo(name = "listOfSubscriptions")   var listOfSubscriptions: List<String> = listOf(""),
    @ColumnInfo(name = "bio")                   var bio: String = "",
    @ColumnInfo(name = "country")               var country: String = "",
    @ColumnInfo(name = "language")              var language: String = "",
    @ColumnInfo(name = "dateOfBirth")           var dateOfBirth: Long = 0,
    @ColumnInfo(name = "maxRange")              var maxRange: Int = 0,
    @ColumnInfo(name = "avgRange")              var avgRange: Int = 0,
    @ColumnInfo(name = "totalTime")             var totalTime: Int = 0
)