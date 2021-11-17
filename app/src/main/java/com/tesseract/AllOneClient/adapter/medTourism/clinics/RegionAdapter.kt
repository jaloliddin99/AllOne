package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.home.RegionRegionAdapter
import com.tesseract.AllOneClient.databinding.LayoutMedTourismRegionsItemBinding
import com.tesseract.AllOneClient.databinding.LayoutSearchItemBinding
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import java.util.*
import kotlin.collections.ArrayList

class RegionAdapter(
    var searchItemList: List<GetRegionDetails>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RegionAdapter.RegionItemViewHolder>() , Filterable {

    var locationFilter = ArrayList<GetRegionDetails>()

    init {
        locationFilter = searchItemList as ArrayList<GetRegionDetails>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionItemViewHolder {
        val binding=
            LayoutMedTourismRegionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RegionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionItemViewHolder, position: Int) {
        val searchItem : GetRegionDetails =locationFilter[position]
        holder.bind(searchItem)
    }

    override fun getItemCount()=locationFilter.size

    inner class RegionItemViewHolder(private val itemBinding: LayoutMedTourismRegionsItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(searchItemBinding: GetRegionDetails) {
            itemBinding.textView.text=searchItemBinding.name
        }

        init {
            locationFilter = searchItemList as ArrayList<GetRegionDetails>
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val searchItemBinding=locationFilter[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClick(searchItemBinding)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(searchItemBinding: GetRegionDetails)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    searchItemList as ArrayList<GetRegionDetails>
                } else {
                    val resultList = ArrayList<GetRegionDetails>()
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
                locationFilter = results?.values as ArrayList<GetRegionDetails>
                notifyDataSetChanged()
            }

        }
    }



}