package app.seals.sealsgallery.domain.images

import android.content.Context
import android.database.Cursor
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import app.seals.sealsgallery.domain.models.ImageDomainModel
import app.seals.sealsgallery.domain.models.TrackDomainModel
import com.google.android.gms.maps.model.LatLng

class ImagesPicker(
    context: Context
) {
    private val cr = context.contentResolver

    fun invoke(track: TrackDomainModel): MutableList<ImageDomainModel> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val cID: Int
        val images: MutableList<ImageDomainModel> = mutableListOf()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        var imageId: Long
        cursor = cr.query(
            uri,
            projection,
            "${MediaStore.Images.Media.DATE_TAKEN} >= ${track.startTime} AND ${MediaStore.Images.Media.DATE_TAKEN} <= ${track.endTime}",
            null,
            null)
        if (cursor != null) {
            cID = cursor.getColumnIndexOrThrow(projection[0])
            while (cursor.moveToNext()) {
                imageId = cursor.getLong(cID)
                val uriImage = Uri.withAppendedPath(uri, "$imageId")
                val originalUri = MediaStore.setRequireOriginal(uriImage)
                val ei = ExifInterface(cr.openInputStream(originalUri)!!)
                val latLong = FloatArray(2)
                if(ei.getLatLong(latLong)){
                    ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    val latLngImage = LatLng(latLong[0].toDouble(),latLong[1].toDouble())
                    images.add(
                        ImageDomainModel(
                            uri = uriImage,
                            latLng = latLngImage,
                            orientation = orientation
                        )
                    )
                }
            }
            cursor.close()
        }
        return images
    }
}