package app.seals.sealsgallery.ui.mytracks.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import app.seals.sealsgallery.R
import app.seals.sealsgallery.domain.models.TrackDomainModel
import java.time.Instant
import java.time.ZoneId

class TrackListRecyclerAdapter (
    private val tracks : MutableLiveData<List<TrackDomainModel>>,
    private val context: Context
) : RecyclerView.Adapter<TrackListRecyclerAdapter.ViewHolder>() {

    val selectedItem = MutableLiveData<TrackDomainModel>()

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val city: TextView = item.findViewById(R.id.trackItemCity)
        val time: TextView = item.findViewById(R.id.trackItemTime)
        val id: TextView = item.findViewById(R.id.trackId)
    }

    override fun getItemCount(): Int = tracks.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = tracks.value?.get(position) ?: TrackDomainModel()
        material.run{
            try {
                holder.city.text = "${
                    Geocoder(context).getFromLocation(
                    this.trackPoints[0].latitude,
                    this.trackPoints[0].longitude,
                    1)[0]
                    .locality} - ${
                    Geocoder(context).getFromLocation(
                    this.trackPoints[trackPoints.size-1].latitude,
                    this.trackPoints[trackPoints.size-1].longitude,
                    1)[0]
                    .locality}"
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val t = Instant.ofEpochSecond(this.startTime/1000)
                .atZone(ZoneId.systemDefault())
            holder.id.text = "${this.startTime}"
            holder.time.text = "${t.dayOfMonth}.${t.monthValue}.${t.year} ${t.hour}:${t.minute}"
            holder.itemView.setOnClickListener {
                selectedItem.postValue(material)
            }
        }
    }
}