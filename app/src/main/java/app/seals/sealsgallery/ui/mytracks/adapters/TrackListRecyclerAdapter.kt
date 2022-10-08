package app.seals.sealsgallery.ui.mytracks.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Geocoder
import android.util.Log
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
import java.time.format.DateTimeFormatter

@SuppressLint("SetTextI18n")
class TrackListRecyclerAdapter (
    private val tracks : MutableLiveData<List<TrackDomainModel>>,
    private val context: Context
) : RecyclerView.Adapter<TrackListRecyclerAdapter.ViewHolder>() {

    val selectedItem = MutableLiveData<TrackDomainModel>()
    val removeItem = MutableLiveData(Pair(-1L, -1))
    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val city: TextView = item.findViewById(R.id.trackItemCity)
        val time: TextView = item.findViewById(R.id.trackItemTime)
    }

    override fun getItemCount(): Int = tracks.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return ViewHolder(itemView)
    }
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.itemView.isSelected = selectedPosition == holder.adapterPosition
        val material = tracks.value?.get(position) ?: TrackDomainModel()
        material.run{
            try {
                holder.city.text = "$startTime ${
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

            val t1 = Instant.ofEpochSecond(this.startTime/1000)
                .atZone(ZoneId.systemDefault())
            val t2 = Instant.ofEpochSecond(this.endTime/1000)
                .atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            holder.time.text = "${t1.format(formatter)} - ${t2.format(formatter)}"
            holder.itemView.setOnClickListener {
                selectedItem.postValue(material)
                notifyItemChanged(selectedPosition)
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition)
            }

            holder.itemView.setOnLongClickListener {
                Log.e("TLRA_", material.startTime.toString())
                val builder = AlertDialog.Builder(context).apply {
                    setTitle("Remove?")
                    setMessage("Are you sure?")
                    setPositiveButton("Yes") { _, _ ->
                        removeItem.postValue(Pair(material.startTime, position))
                    }
                    setNegativeButton("No") { _, _ ->
                    }
                }
                builder.show()
                false
            }
        }
    }

    fun selectLastItem() {
        val size = tracks.value?.size ?: 0
        if(size > 0 ) {
            selectedItem.postValue(tracks.value?.last() ?: TrackDomainModel())
            selectedPosition = size - 1
        }
    }
}