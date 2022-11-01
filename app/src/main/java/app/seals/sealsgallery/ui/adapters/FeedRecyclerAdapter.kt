package app.seals.sealsgallery.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.PostDomainModel
import app.seals.sealsgallery.ui.helpers.ShowPostModel
import org.koin.java.KoinJavaComponent.inject

@SuppressLint("SetTextI18n")
class FeedRecyclerAdapter(
    private val feed: MutableLiveData<List<PostDomainModel>>
) : RecyclerView.Adapter<FeedRecyclerAdapter.ViewHolder>() {

    private val showPostModel: ShowPostModel by inject(ShowPostModel::class.java)
    val selectedItem = MutableLiveData(-1)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = feed.value?.get(position) ?: PostDomainModel()
        showPostModel.invoke(holder.itemView, material)
        holder.itemView.setOnClickListener {
            selectedItem.postValue(position)
        }
    }

    override fun getItemCount(): Int = feed.value?.size ?: 0

}