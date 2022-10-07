package app.seals.sealsgallery.data.room

import androidx.room.TypeConverter
import app.seals.sealsgallery.data.models.TrackPointDataModel
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

}