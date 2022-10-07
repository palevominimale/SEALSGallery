package app.seals.sealsgallery.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.map_tools.SetStartEndMarkers
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordFragment : Fragment() {

    private val vm : RecordViewModel by viewModel()
    private val setMarkers : SetStartEndMarkers by inject()
    private lateinit var map : MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.initReceiver()
        map = view.rootView.findViewById(R.id.recordMapView)
        map.onCreate(savedInstanceState)
        map.onResume()
        MapsInitializer.initialize(requireContext())
        vm.currentRecord.observe(viewLifecycleOwner) {
            map.getMapAsync { googleMap ->
                googleMap.apply {
                    val options = vm.drawTrack()
                    val markers = setMarkers.invoke(options)
                    clear()
                    addPolyline(options)
                    moveCamera(vm.updateCameraBounds())
                    addMarker(markers.first)
                    addMarker(markers.second)
                }
            }
        }
    }
}