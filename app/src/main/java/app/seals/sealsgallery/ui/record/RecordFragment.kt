package app.seals.sealsgallery.ui.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.R
import app.seals.sealsgallery.databinding.FragmentRecordBinding
import app.seals.sealsgallery.domain.bootstrap.CheckPermissions
import app.seals.sealsgallery.location.LocationService
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.firebase.database.FirebaseDatabase
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
        map = view.rootView.findViewById(R.id.recordMapView)
        map.onCreate(savedInstanceState)
        map.onResume()
        MapsInitializer.initialize(requireContext())
        vm.setObservedTrack()
        vm.currentRecord.observe(viewLifecycleOwner) { track ->
            map.getMapAsync { googleMap ->
                googleMap.apply {
                    clear()
                    addPolyline(vm.drawTrack(track))
                    moveCamera(vm.updateCameraBounds(track))
                }
            }

        }
    }


}