package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutSavedAddressesBinding
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.SavedLocationData

class AdapterSavedLocations(
    private val carImageList: List<SavedLocationData>,
    private val listener: OnLocationClickListener
)
    : RecyclerView.Adapter<AdapterSavedLocations.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutSavedAddressesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : SavedLocationData =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutSavedAddressesBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(carImage: SavedLocationData) {
            itemBinding.textPlace.text=carImage.name
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val type: SavedLocationData =carImageList[adapterPosition]
            if (position!= RecyclerView.NO_POSITION){
                listener.onItemClick(type)
            }
        }

    }

    interface OnLocationClickListener{
        fun onItemClick(type: SavedLocationData?)
    }
}