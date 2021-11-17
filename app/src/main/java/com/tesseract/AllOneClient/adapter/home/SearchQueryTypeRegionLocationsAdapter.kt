package com.tesseract.AllOneClient.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutGetRegionSearchItemBinding
import com.tesseract.AllOneClient.model.home.LocationSearch.LocationSearchList
import java.util.*
import kotlin.collections.ArrayList

class SearchQueryTypeRegionLocationsAdapter(
    var searchItemList: List<LocationSearchList>,
    private val listener: OnItemClickListener
)


    : RecyclerView.Adapter<SearchQueryTypeRegionLocationsAdapter.DistrictItemViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictItemViewHolder {
        val binding=
            LayoutGetRegionSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DistrictItemViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DistrictItemViewHolder, position: Int) {
        val searchItem : LocationSearchList =searchItemList[position]
        holder.bind(searchItem)
    }

    override fun getItemCount()=searchItemList.size


    inner class DistrictItemViewHolder(private val itemBinding: LayoutGetRegionSearchItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(searchItemBinding: LocationSearchList) {
            itemBinding.locationName.text=searchItemBinding.address
            itemBinding.locationBelowId.text=searchItemBinding.region
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            val locationModel:LocationSearchList=searchItemList[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClick2(position, locationModel)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick2(position: Int, districtName: LocationSearchList)
    }


}