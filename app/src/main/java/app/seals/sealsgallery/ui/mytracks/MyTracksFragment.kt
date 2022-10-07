package app.seals.sealsgallery.ui.mytracks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import app.seals.sealsgallery.ui.mytracks.adapters.TrackListRecyclerAdapter
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MyTracksFragment : Fragment() {

    private val vm : MyTracksViewModel by viewModel()
    private val setStartEndMarkers : SetStartEndMarkers by inject()
    private lateinit var map : MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_tracks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tracksListSwipe = view.rootView.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val tracksListRecycler = view.rootView.findViewById<RecyclerView>(R.id.tracksListRecycler)
        val tracksListAdapter = TrackListRecyclerAdapter(vm.tracks, requireContext())
        map = view.rootView.findViewById(R.id.tracksMapView)
        map.onCreate(savedInstanceState)
        map.onResume()
        MapsInitializer.initialize(requireContext())
        vm.initReceiver()
        vm.loadCachedTracks()
        map.getMapAsync { googleMap ->
            googleMap.setOnMarkerClickListener {
                if (it.title != null) {
                    startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(it.title)))
                }
                return@setOnMarkerClickListener true
            }
        }

        tracksListAdapter.selectedItem.observe(viewLifecycleOwner) { track ->
            map.getMapAsync { googleMap ->
                googleMap.apply {
                    val options = vm.drawTrack(track)
                    val markers = setStartEndMarkers.invoke(options)
                    clear()
                    addPolyline(options)
                    moveCamera(vm.updateCameraBounds(track))
                    addMarker(markers.first)
                    addMarker(markers.second)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    vm.loadPhotos(track).collect { marker ->
                        requireActivity().runOnUiThread {
                            googleMap.addMarker(marker)
                        }
                    }
                }
            }
        }

        tracksListSwipe.setOnRefreshListener {
            vm.loadTracksFromFirebase()
            tracksListSwipe.isRefreshing = false
        }
        tracksListRecycler.layoutManager = LinearLayoutManager(requireContext()).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        tracksListRecycler.adapter = tracksListAdapter
        vm.tracks.observe(viewLifecycleOwner) {
            tracksListAdapter.notifyItemRangeChanged(0, it.size-1)
            tracksListAdapter.selectLastItem()
        }
    }
}