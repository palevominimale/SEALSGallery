package app.seals.sealsgallery.ui.mytracks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.seals.sealsgallery.R
import app.seals.sealsgallery.ui.mytracks.adapters.TrackListRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyTracksFragment : Fragment() {

    private val vm : MyTracksViewModel by viewModel()
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_tracks, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tracksListRecycler = view.rootView.findViewById<RecyclerView>(R.id.tracksListRecycler)
        val tracksListAdapter = TrackListRecyclerAdapter(vm.tracks, requireContext())

        tracksListRecycler.layoutManager = LinearLayoutManager(requireContext())
        tracksListRecycler.adapter = tracksListAdapter
        vm.loadTracks()
        tracksListAdapter.notifyDataSetChanged()

        vm.tracks.observe(viewLifecycleOwner) {
            tracksListAdapter.notifyDataSetChanged()
        }
    }
}