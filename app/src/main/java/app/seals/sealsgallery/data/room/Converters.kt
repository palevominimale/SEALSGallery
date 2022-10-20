package app.seals.sealsgallery.data.room

import androidx.room.TypeConverter
import app.seals.sealsgallery.data.models.PostDataModel
import app.seals.sealsgallery.data.models.TrackDataModel
import app.seals.sealsgallery.data.models.TrackPointDataModel
import app.seals.sealsgallery.data.models.UserDataModel
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class Converters {

    private var gson: Gson = Gson()
    @TypeConverter
    fun stringToTrackPointDataList(data: String): List<TrackPointDataModel> {
        val listType: Type = object : TypeToken<List<TrackPointDataModel>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun trackPointDataListToString(list: List<TrackPointDataModel>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun stringToListStrings(data: String): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }
    @TypeConverter
    fun listStringToString(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun listFeedToString(list: List<PostDataModel>) : String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun userToString(user: UserDataModel) : String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun stringToUser(data: String?) : UserDataModel {
        if(data == null || data.isEmpty()) {
            return UserDataModel()
        }
        val listType: Type = object : TypeToken<UserDataModel>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun stringToListFeed(data: String?) : List<PostDataModel> {
        if(data == null || data.isEmpty()) {
            return listOf()
        }
        val listType: Type = object : TypeToken<List<PostDataModel>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun trackToString(track: TrackDataModel): String {
        return gson.toJson(track)
    }

    @TypeConverter
    fun stringToTrack(data: String?): TrackDataModel {
        if(data == null || data.isEmpty()) {
            return TrackDataModel()
        }
        val listType: Type = object : TypeToken<TrackDataModel>() {}.type
        return gson.fromJson(data, listType)
    }

}