package app.seals.sealsgallery.ui.feed.main.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.ui.feed.main.FeedViewModel
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragment
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("SetTextI18n")
class FeedRecyclerAdapter(
    private val feed: MutableLiveData<List<PostDomainModel>>,
    private val context: Context,
    private val savedInstanceState: Bundle?,
    private val vm: FeedViewModel,
    private val activity: Activity
) : RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder>() {

    private val setStartEndMarkers : SetStartEndMarkers by inject(SetStartEndMarkers::class.java)
    val selectedItem = MutableLiveData<Int>(-1)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val userName: TextView = item.findViewById(R.id.userItemName)
        val lastOnline: TextView = item.findViewById(R.id.userItemLastOnline)
        val trackTime: TextView = item.findViewById(R.id.userTrackTime)
        val userAvatar: ImageView = item.findViewById(R.id.userItemAvatar)
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
            var userPhoto : RoundedBitmapDrawable? = null
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = kotlin.runCatching {
                    Picasso.get().load(material.user.photoLink).get()
                }
                val bitmapGet = bitmap.getOrNull()
                userPhoto = if(bitmapGet != null) {
                    RoundedBitmapDrawableFactory.create(context.resources, bitmapGet)
                        .apply {
                            isCircular = true
                        }
                } else null
            }.invokeOnCompletion {
                activity.runOnUiThread {
                    if (userPhoto != null) {
                        holder.userAvatar.setImageDrawable(userPhoto)
                    } else {
                        holder.userAvatar.setImageResource(R.drawable.ic_baseline_person_24)
                    }
                }
            }
            holder.userName.text = material.user.name
            holder.lastOnline.text = "Last seen ${lastOnlineTime.format(formatterLastOnline)}"
            holder.trackTime.text = "${t1.format(formatter)} - ${t2.format(formatter)}"
            if(track.trackPoints.size > 0) {
                holder.trackCity.text = Geocoder(context).getFromLocation(
                    track.trackPoints[0].latitude,
                    track.trackPoints[0].longitude,
                    1)[0]
                    .locality
            }
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
            holder.itemView.setOnClickListener {
                selectedItem.postValue(position)
            }
        }
    }

    override fun getItemCount(): Int = feed.value?.size ?: 0

}