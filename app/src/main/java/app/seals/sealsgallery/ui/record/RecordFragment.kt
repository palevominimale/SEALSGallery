package app.seals.sealsgallery.ui.record

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.databinding.FragmentRecordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecordFragment : Fragment() {

    private lateinit var binding : FragmentRecordBinding
    private val vm : RecordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}