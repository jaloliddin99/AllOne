package com.tesseract.AllOneClient.adapter.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutDistrictSearchItemBinding
import com.tesseract.AllOneClient.databinding.LayoutSearchItemBinding
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import java.util.*
import kotlin.collections.ArrayList


class SearchDistrictAdapter(
    var searchItemList: List<DistrictList>,
    private val listener: OnItemClick
) : RecyclerView.Adapter<SearchDistrictAdapter.DistrictItemViewHolder>() , Filterable{

    var locationFilter = ArrayList<DistrictList>()
    private var selectedPosition = -1
    init {
        locationFilter = searchItemList as ArrayList<DistrictList>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictItemViewHolder {
        val binding=
            LayoutDistrictSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return DistrictItemViewHolder(binding)

    }

    override fun onBindViewHolder(holder: DistrictItemViewHolder, position: Int) {
        val searchItem : DistrictList =locationFilter[position]
        holder.bind(searchItem)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount()=locationFilter.size


    inner class DistrictItemViewHolder(private val itemBinding: LayoutDistrictSearchItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(searchItemBinding: DistrictList) {
            itemBinding.searchItemText.text=searchItemBinding.name

            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                val position:String=locationFilter[adapterPosition].id.toString()
                val districtName:String?=locationFilter[adapterPosition].name
                if (adapterPosition!= RecyclerView.NO_POSITION){
                    listener.onItemClick(position, districtName)
                }
                itemBinding.imageTrueItem.visibility=View.VISIBLE
                itemBinding.locationClick.setBackgroundResource(R.drawable.district_search_item_selected)
            } else {
                itemView.isSelected = false
                itemBinding.imageTrueItem.visibility=View.GONE
                itemBinding.locationClick.setBackgroundResource(R.drawable.bg_grey_rounded)
            }
        }

        init {
            locationFilter = searchItemList as ArrayList<DistrictList>
        }


    }

    interface OnItemClick{
        fun onItemClick(position: String, districtName: String?)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    searchItemList as ArrayList<DistrictList>
                } else {
                    val resultList = ArrayList<DistrictList>()
                    for (row in searchItemList) {
                        if (row.name?.lowercase(Locale.ROOT)
                                ?.contains(charSearch.lowercase(Locale.ROOT)) == true
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = locationFilter
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                locationFilter = results?.values as ArrayList<DistrictList>
                notifyDataSetChanged()
            }

        }
    }



}