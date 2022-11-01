package app.seals.sealsgallery.ui.mytracks_feed_based

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.seals.sealsgallery.R
import app.seals.sealsgallery.ui.feed.main.FeedFragment
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyTracksFeedFragment : Fragment() {

    private val vm: MyTracksFeedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("MTFF", "start for ${vm.auth.uid}")
        return inflater.inflate(R.layout.fragment_my_tracks_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.loadFeed()
        vm.userFeed.observe(viewLifecycleOwner) { list ->
            list.forEach { post ->
                Log.e("MTFF", "$post")
            }
        }
    }
}