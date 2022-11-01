package app.seals.sealsgallery.ui.user_feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.ui.adapters.FeedRecyclerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFeedFragment : Fragment() {

    private val vm: UserFeedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("MTFF", "start for ${vm.auth.uid}")
        return inflater.inflate(R.layout.fragment_my_tracks_feed, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.userpageRecyclerView)
        val adapter = FeedRecyclerAdapter(vm.userFeed)
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        vm.loadFeed()
        vm.userFeed.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }
    }
}