package app.seals.sealsgallery.domain.map_tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.res.ResourcesCompat
import app.seals.sealsgallery.R
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class SetMarkers(
    private val context: Context
) {

    fun invoke(options: PolylineOptions) : Pair<MarkerOptions, MarkerOptions> {
        val startMarker = MarkerOptions().apply {
            position(options.points.first())
            icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.location_on_48px)!!))

        }
        val endMarker = MarkerOptions().apply {
            position(options.points.last())
            icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.where_to_vote_48px)!!))
        }
        return Pair(startMarker, endMarker)
    }

    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable = ResourcesCompat.getDrawable(context.resources, drawableRes, context.resources.newTheme())
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable?.intrinsicWidth ?: 30,
            drawable?.intrinsicHeight ?: 30,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable?.draw(canvas)
        return bitmap
    }

}