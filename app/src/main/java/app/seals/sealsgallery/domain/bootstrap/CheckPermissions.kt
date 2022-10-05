package app.seals.sealsgallery.domain.bootstrap


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class CheckPermissions(
    private val context: Context,
    private val activity: Activity,
) {
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
    )
    private val granted = PackageManager.PERMISSION_GRANTED

    fun invoke() : Boolean {
        if (
            ActivityCompat.checkSelfPermission(context, permissions[0]) != granted
            && ActivityCompat.checkSelfPermission(context, permissions[1]) != granted
            && ActivityCompat.checkSelfPermission(context, permissions[2]) != granted
            && ActivityCompat.checkSelfPermission(context, permissions[3]) != granted
            && ActivityCompat.checkSelfPermission(context, permissions[4]) != granted
        ) {
            ActivityCompat.requestPermissions(activity, permissions,1)
        }
        return !(ActivityCompat.checkSelfPermission(context, permissions[0]) != granted
                && ActivityCompat.checkSelfPermission(context, permissions[1]) != granted
                && ActivityCompat.checkSelfPermission(context, permissions[2]) != granted
                && ActivityCompat.checkSelfPermission(context, permissions[3]) != granted
                && ActivityCompat.checkSelfPermission(context, permissions[4]) != granted)
    }
}