package app.seals.sealsgallery.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.databinding.FragmentGalleryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    private lateinit var binding : FragmentGalleryBinding
    private val vm : FeedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        vm.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}