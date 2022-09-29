package app.seals.sealsgallery.ui.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.databinding.FragmentRecordBinding
import app.seals.sealsgallery.domain.bootstrap.CheckPermissions
import app.seals.sealsgallery.location.LocationService
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private lateinit var checkPermissions: CheckPermissions
    private val vm : RecordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkPermissions = CheckPermissions(requireContext(), requireActivity())
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun startRecord() {
        if(checkPermissions.invoke()) {
            val intent = Intent(requireContext(), LocationService::class.java)
            requireActivity().startService(intent)
        }
    }
}