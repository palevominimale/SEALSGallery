package app.seals.sealsgallery.ui.feed.show_single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.PostDomainModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
}