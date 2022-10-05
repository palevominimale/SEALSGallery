package app.seals.sealsgallery.domain.images

import android.graphics.*
import androidx.exifinterface.media.ExifInterface

class ImagesOperations {

    fun normalizeBitmap(orientation: Int, bitmap: Bitmap) : Bitmap {
        return when(orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(getRoundedCornerBitmap(bitmap), 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(getRoundedCornerBitmap(bitmap), 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(getRoundedCornerBitmap(bitmap), 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipBitmap(getRoundedCornerBitmap(bitmap),
                horizontal = true,
                vertical = false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipBitmap(getRoundedCornerBitmap(bitmap),
                horizontal = false,
                vertical = true)
            else -> getRoundedCornerBitmap(bitmap)
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flipBitmap(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap {
        val matrix = Matrix()
        matrix.preScale(if (horizontal) -1f else 1f, if (vertical) -1f else 1f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width,
            bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = 75f
        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

}