package app.seals.sealsgallery.location

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class LocationService : Service() {

    companion object {
        private const val INTERVAL = 3000L
        private const val TAG = "LOCATION_SERVICE"
    }
    private lateinit var flc: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        flc = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.e(TAG, "${locationResult.lastLocation}")
                super.onLocationResult(locationResult)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        requestLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        flc.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
}