package app.seals.sealsgallery.ui.feed.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.ui.feed.FeedViewModel
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import org.koin.java.KoinJavaComponent.inject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("SetTextI18n")
class FeedRecyclerAdapter(
    private val feed: MutableLiveData<List<PostDomainModel>>,
    private val context: Context,
    private val savedInstanceState: Bundle?,
    private val vm: FeedViewModel
) : RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder>() {

    private val setStartEndMarkers : SetStartEndMarkers by inject(SetStartEndMarkers::class.java)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val userName: TextView = item.findViewById(R.id.userItemName)
        val lastOnline: TextView = item.findViewById(R.id.userItemLastOnline)
        val trackTime: TextView = item.findViewById(R.id.userTrackTime)
//        val userAvatar: ImageView = item.findViewById(R.id.userItemAvatar)
        val trackCity: TextView = item.findViewById(R.id.feedItemCity)
        val itemMap: MapView = item.findViewById(R.id.userItemMapView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = feed.value?.get(position) ?: PostDomainModel()
        material.run {
            val t1 = Instant.ofEpochSecond(track.startTime/1000)
                .atZone(ZoneId.systemDefault())
            val t2 = Instant.ofEpochSecond(track.endTime/1000)
                .atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm")
            val formatterLastOnline = DateTimeFormatter.ofPattern("dd/MM HH:mm")
            val lastOnlineTime = Instant.ofEpochSecond(material.user.lastLogin)
                .atZone(ZoneId.systemDefault())
            holder.userName.text = material.user.name
            holder.lastOnline.text = lastOnlineTime.format(formatterLastOnline)
            holder.trackTime.text = "${t1.format(formatter)} - ${t2.format(formatter)}"
            holder.trackCity.text = "city 17"
            holder.trackCity.text = Geocoder(context).getFromLocation(
                track.trackPoints[0].latitude,
                track.trackPoints[0].longitude,
                1)[0]
                .locality
            holder.itemMap.onCreate(savedInstanceState)
            holder.itemMap.onResume()
            MapsInitializer.initialize(context)
            holder.itemMap.getMapAsync { googleMap ->
                googleMap.apply {
                    val options = vm.drawTrack(track)
                    val markers = setStartEndMarkers.invoke(options)
                    clear()
                    addPolyline(options)
                    moveCamera(vm.updateCameraBounds(track))
                    addMarker(markers.first)
                    addMarker(markers.second)
                }
            }
        }
    }

    override fun getItemCount(): Int = feed.value?.size ?: 0

}