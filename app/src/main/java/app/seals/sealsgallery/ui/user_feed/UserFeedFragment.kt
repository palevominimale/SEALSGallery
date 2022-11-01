package app.seals.sealsgallery.ui.user_feed

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.ui.adapters.FeedRecyclerAdapter
import app.seals.sealsgallery.ui.feed.show_single.ShowFeedItemFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFeedFragment : Fragment() {

    private val vm: UserFeedViewModel by viewModel()
    private val showItem: ShowFeedItemFragment by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_my_tracks_feed, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.userpageRecyclerView)
        val userAvatar = view.findViewById<ImageView>(R.id.userpageAvatar)
        val userName = view.findViewById<TextView>(R.id.userpageUsername)
        val userSubscribers = view.findViewById<TextView>(R.id.userpageSubscribers)
        val userSubscriptions = view.findViewById<TextView>(R.id.userpageSubscriptions)
        val userTracksNumber = view.findViewById<TextView>(R.id.userpageTotalTracks)
        val adapter = FeedRecyclerAdapter(vm.userFeed)

        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(requireContext())
        vm.loadFeed()
        vm.loadUserData()

        vm.userFeed.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        vm.userData.observe(viewLifecycleOwner) {
            var userPhoto : RoundedBitmapDrawable? = null
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = kotlin.runCatching {
                    Picasso.get().load(it.photoLink).get()
                }
                val bitmapGet = bitmap.getOrNull()
                userPhoto = if(bitmapGet != null) {
                    RoundedBitmapDrawableFactory.create(requireContext().resources, bitmapGet)
                        .apply {
                            isCircular = true
                        }
                } else null
            }.invokeOnCompletion {
                requireActivity().runOnUiThread {
                    if (userPhoto != null) {
                        userAvatar.setImageDrawable(userPhoto)
                    } else {
                        userAvatar.setImageResource(R.drawable.ic_baseline_person_24)
                    }
                }
            }
            userName.text = it.name
            userSubscriptions.text = it.listOfSubscriptions.size.toString()
            userSubscribers.text = it.listOfSubscribers.size.toString()
            userTracksNumber.text = vm.userFeed.value?.size.toString()
        }

        adapter.selectedItem.observe(viewLifecycleOwner) {
            if(it>=0) {
                showItem.showPost(parentFragmentManager, vm.userFeed.value?.get(it))
            }
        }
    }
}