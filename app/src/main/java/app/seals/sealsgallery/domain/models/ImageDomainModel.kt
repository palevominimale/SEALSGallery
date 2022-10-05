package app.seals.sealsgallery.domain.models

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class ImageDomainModel (
    val uri: Uri? = null,
    val latLng: LatLng? = null
) : Serializable