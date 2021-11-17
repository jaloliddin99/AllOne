package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.SearchDistrictAdapter
import com.tesseract.AllOneClient.databinding.LayoutClinicDropdownItemBinding
import com.tesseract.AllOneClient.databinding.LayoutDistrictSearchItemBinding
import com.tesseract.AllOneClient.databinding.LayoutMedTurDistrictBinding
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import java.util.*
import kotlin.collections.ArrayList

class DistrictAdapter (
    var searchItemList: List<DistrictList>,
    private val listener: OnItemClick
) : RecyclerView.Adapter<DistrictAdapter.DistrictItemViewHolder>() , Filterable {

    var locationFilter = ArrayList<DistrictList>()
    private var selectedPosition = -1
    init {
        locationFilter = searchItemList as ArrayList<DistrictList>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictItemViewHolder {
        val binding=
            LayoutClinicDropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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


    inner class DistrictItemViewHolder(private val itemBinding: LayoutClinicDropdownItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(searchItemBinding: DistrictList) {
            itemBinding.textView.text=searchItemBinding.name

            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                val position:DistrictList=locationFilter[adapterPosition]
                if (adapterPosition!= RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }

            } else {
                itemView.isSelected = false

            }
        }

        init {
            locationFilter = searchItemList as ArrayList<DistrictList>
        }
    }

    interface OnItemClick{
        fun onItemClick(district:DistrictList)
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