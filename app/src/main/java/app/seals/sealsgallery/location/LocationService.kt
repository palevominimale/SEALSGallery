package app.seals.sealsgallery.location

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import app.seals.sealsgallery.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.Instant

class LocationService : Service() {

    companion object {
        private const val INTERVAL = 5000L
        private const val MIN_INTERVAL = 3000L
        private const val MAX_INTERVAL = 30000L
        private const val TAG = "LOCATION_SERVICE"
        private val db = FirebaseDatabase.getInstance()
        private val auth = FirebaseAuth.getInstance()
        private val now = Instant.now().epochSecond
        private val ref = db.getReference("tracks").child(auth.currentUser?.uid.toString()).child(now.toString())
    }

    private lateinit var flc: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        setupNotifications()
        flc = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                Log.e(TAG, "${locationResult.lastLocation}")
                ref.child(locationResult.lastLocation?.time.toString())
                    .setValue(locationResult.lastLocation)
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
            maxWaitTime = MAX_INTERVAL
            fastestInterval = MIN_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        flc.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun setupNotifications() {
        val channelId = "my_channel_01"
        val channel = NotificationChannel(channelId,
            "Channel title",
            NotificationManager.IMPORTANCE_HIGH)

        val ns = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ns.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId).apply {
            setContentTitle("SEALS Gallery")
            setContentTitle("SEALS Gallery is active")
            setContentText("Tracking is active now!")
            setSmallIcon(R.drawable.radio_button_checked_40px)
        }.build()

        startForeground(1, notification)
    }
}