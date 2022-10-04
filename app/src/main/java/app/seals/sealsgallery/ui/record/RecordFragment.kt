package app.seals.sealsgallery.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.R
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordFragment : Fragment() {

    private lateinit var map : MapView
    private val vm : RecordViewModel by viewModel()

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
                    clear()
                    addPolyline(options)
                    moveCamera(vm.updateCameraBounds())
                }
            }
        }
    }
}