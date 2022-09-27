package app.seals.sealsgallery.ui.mytracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyTracksFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val vm : MyTracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}