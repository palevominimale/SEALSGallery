package app.seals.sealsgallery.ui.feed.main

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
import app.seals.sealsgallery.ui.feed.main.adapters.FeedRecyclerAdapter
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    private val vm : FeedViewModel by viewModel()
    private val showItem: ShowFeedItemFragment by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val feedSwipe = view.rootView.findViewById<SwipeRefreshLayout>(R.id.feedSwipeRefresh)
        val feedRecycler = view.rootView.findViewById<RecyclerView>(R.id.feedRecycler)
        val feedAdapter = FeedRecyclerAdapter(vm.feed)
        feedRecycler.adapter = feedAdapter
        feedRecycler.layoutManager = LinearLayoutManager(requireContext())

        vm.feed.observe(viewLifecycleOwner) {
            feedRecycler.adapter?.notifyDataSetChanged()
        }

        feedSwipe.setOnRefreshListener {
            vm.loadFeedFromFirebase()
            feedSwipe.isRefreshing = false
        }

        vm.loadFeed()

        feedAdapter.selectedItem.observe(viewLifecycleOwner) {
            if(it>=0) {
                showItem.showPost(parentFragmentManager, vm.feed.value?.get(it))
            }
        }
    }
}