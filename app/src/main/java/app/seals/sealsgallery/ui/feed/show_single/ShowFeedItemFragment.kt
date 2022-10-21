package app.seals.sealsgallery.ui.feed.show_single

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.PostDomainModel
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ShowFeedItemFragment : DialogFragment() {

    private val vm : ShowFeedItemFragmentViewModel by viewModel()
    private var material : PostDomainModel? = null

    fun showPost(manager: FragmentManager, post: PostDomainModel?) {
        super.show(manager, "tag")
        material = post
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_feed_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (material != null) {
            vm.show(view, material!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun load(view: View, material: PostDomainModel) {
        val userName: TextView = view.findViewById(R.id.userItemName)
        val lastOnline: TextView = view.findViewById(R.id.userItemLastOnline)
        val trackTime: TextView = view.findViewById(R.id.userTrackTime)
        val userAvatar: ImageView = view.findViewById(R.id.userItemAvatar)
        val trackCity: TextView = view.findViewById(R.id.feedItemCity)
        val itemMap: MapView = view.findViewById(R.id.userItemMapView)

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
                    RoundedBitmapDrawableFactory.create(requireContext().resources, bitmapGet)
                        .apply {
                            isCircular = true
                        }
                } else null
            }.invokeOnCompletion {
                requireActivity().runOnUiThread {
                    if (userPhoto != null) {
                        userAvatar.setImageDrawable(userPhoto)
                    } else {
                        userAvatar.setImageResource(R.drawable.ic_baseline_person_24)
                    }
                }
            }
            userName.text = material.user.name
            lastOnline.text = "Last seen ${lastOnlineTime.format(formatterLastOnline)}"
            trackTime.text = "${t1.format(formatter)} - ${t2.format(formatter)}"
            if(track.trackPoints.size > 0) {
                trackCity.text = Geocoder(context).getFromLocation(
                    track.trackPoints[0].latitude,
                    track.trackPoints[0].longitude,
                    1)[0]
                    .locality
            }
            itemMap.onCreate(null)
            itemMap.onResume()
            MapsInitializer.initialize(requireContext())
            itemMap.getMapAsync { googleMap ->
                googleMap.apply {
                    val options = vm.drawTrack(track)
                    val markers = vm.setMarkers(options)
                    clear()
                    addPolyline(options)
                    moveCamera(vm.updateCameraBounds(track))
                    addMarker(markers.first)
                    addMarker(markers.second)
                }
            }
        }
    }

}