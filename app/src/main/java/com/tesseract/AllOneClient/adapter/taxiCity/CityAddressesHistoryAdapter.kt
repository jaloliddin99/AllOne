package com.tesseract.AllOneClient.adapter.taxiCity

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutCityAddressHistoryBinding
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.SavedLocationData

class CityAddressesHistoryAdapter(
    private val carImageList: List<SavedLocationData>,
    private val listener: OnLocationClickListener
)
    : RecyclerView.Adapter<CityAddressesHistoryAdapter.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutCityAddressHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : SavedLocationData =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutCityAddressHistoryBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(carImage: SavedLocationData) {
            when (carImage.type) {
                "home" -> {
                    itemBinding.iconEnd.setImageResource(R.drawable.ic_edit_pen)
                    itemBinding.iconStart.setImageResource(R.drawable.ic_home_icon)
                }
                "work" -> {
                    itemBinding.iconEnd.setImageResource(R.drawable.ic_edit_pen)
                    itemBinding.iconStart.setImageResource(R.drawable.ic_work_bag)
                }
                else -> {
                    itemBinding.iconEnd.visibility=View.GONE
                    itemBinding.iconStart.setImageResource(R.drawable.ic_time_left)
                }

            }
            itemBinding.layout.setOnClickListener {
                listener.onItemClick(carImage)
                Log.i("TAG", "bind: ${carImage.latLng}")
            }
            itemBinding.name.text=carImage.name
            itemBinding.address.text=carImage.address
        }

    }

    interface OnLocationClickListener{
        fun onItemClick(type: SavedLocationData)
    }
}