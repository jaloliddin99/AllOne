package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutCityCarListaBinding
import com.tesseract.AllOneClient.databinding.LayoutCityItemBinding
import com.tesseract.AllOneClient.model.taxiCity.StationModel

class StationAdapter(
    private val mapModelList: List<StationModel>,
    private val listener: OnDeleteListener
) : RecyclerView.Adapter<StationAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding =
            LayoutCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem: StationModel = mapModelList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount() = mapModelList.size

    inner class NewsItemViewHolder(private val itemBinding: LayoutCityItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: StationModel) {
            itemBinding.station.text=model.stationName
            itemBinding.deleteStation.setOnClickListener {
                listener.onDelete(adapterPosition)
            }
        }


    }

    interface OnDeleteListener {
        fun onDelete(position: Int)
    }
}