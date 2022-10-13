package app.seals.sealsgallery.location

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.TrackDomainModel
import app.seals.sealsgallery.domain.models.TrackPointDomainModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.android.inject
import java.time.Instant

@SuppressLint("MissingPermission, UnspecifiedImmutableFlag")
class LocationService : Service() {

    private val context by inject<Context>()

    private val refMainNode = context.getString(R.string.firebase_reference_name)
    private val refTracksNode = context.getString(R.string.firebase_reference_tracks_name)
    private val refTrackPoints = context.getString(R.string.firebase_reference_points_node_name)
    private val refEndTime = context.getString(R.string.firebase_reference_end_time_name)
    private val intentExtraName = context.getString(R.string.track_intent_name)

    private val db = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val now = Instant.now().epochSecond
    private val uid = auth.currentUser?.uid.toString()
    private val ref = db.getReference(refMainNode).child(uid).child(refTracksNode).child(now.toString())
    private val track = TrackDomainModel()
    private var id = 0
    private var interval = context.getSharedPreferences(
        context.getString(R.string.tracker_location_interval_name),
        MODE_PRIVATE).toString()

    companion object {
        private const val MIN_INTERVAL = 3000L
        private const val TAG = "RECORD_SERVICE"
    }

    private lateinit var flc: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate() {
        super.onCreate()
        setupNotifications()
        flc = LocationServices.getFusedLocationProviderClient(this)
        track.trackPoints.clear()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val point = TrackPointDomainModel(
                    id = locationResult.lastLocation?.time ?: 0L,
                    latitude = locationResult.lastLocation?.latitude ?: 0.0,
                    longitude = locationResult.lastLocation?.longitude ?: 0.0,
                    altitude = locationResult.lastLocation?.altitude ?: 0.0,
                )
                ref.child(refTrackPoints).child(id.toString()).setValue(point)
                ref.child(refEndTime).setValue(point.id)
                id++
                track.trackPoints.add(point)
                val intent = Intent()
                intent.action = getString(R.string.track_content_intent)
                intent.putExtra(intentExtraName, track)
                val pi = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                pi.send()
                super.onLocationResult(locationResult)
            }
        }
        db.setPersistenceEnabled(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action == getString(R.string.stop_intent)) {
            flc.removeLocationUpdates(locationCallback)
            stopForeground(true)
            stopSelf()
        } else {
            requestLocation()
            val i = Intent()
            i.action = getString(R.string.start_intent)
            val pi = PendingIntent.getBroadcast(applicationContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
            pi.send()
            Log.e(TAG, "$i")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun requestLocation() {
        track.startTime = Instant.now().toEpochMilli()
        ref.child(track.startTime.toString())
        ref.setValue(track)
        val locationRequest = LocationRequest.create().apply {
            interval = (this@LocationService.interval.toLongOrNull()?: 3L)*1000
            fastestInterval = MIN_INTERVAL
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
        flc.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun setupNotifications() {
        val channelId = getString(R.string.notifications_channel)
        val channel = NotificationChannel(channelId,
            getString(R.string.notifications_channel_title),
            NotificationManager.IMPORTANCE_HIGH)

        val ns = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ns.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId).apply {
            setContentTitle(getString(R.string.app_name))
            setContentTitle(getString(R.string.service_is_active))
            setContentText(getString(R.string.tracker_is_active))
            setSmallIcon(R.drawable.radio_button_checked_40px)
        }.build()

        startForeground(1, notification)
    }
}