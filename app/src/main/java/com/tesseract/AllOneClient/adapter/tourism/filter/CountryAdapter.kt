package com.tesseract.AllOneClient.adapter.tourism.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.medTourism.clinics.RegionAdapter
import com.tesseract.AllOneClient.databinding.LayoutMedTourismRegionsItemBinding
import com.tesseract.AllOneClient.model.tourism.countries.Content
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    var searchItemList: List<Content>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<CountryAdapter.RegionItemViewHolder>() , Filterable {

    var locationFilter = ArrayList<Content>()

    init {
        locationFilter = searchItemList as ArrayList<Content>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionItemViewHolder {
        val binding=
            LayoutMedTourismRegionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RegionItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionItemViewHolder, position: Int) {
        val searchItem : Content =locationFilter[position]
        holder.bind(searchItem)
    }

    override fun getItemCount()=locationFilter.size

    inner class RegionItemViewHolder(private val itemBinding: LayoutMedTourismRegionsItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(searchItemBinding: Content) {
            itemBinding.textView.text=searchItemBinding.name
        }

        init {
            locationFilter = searchItemList as ArrayList<Content>
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
        fun onItemClick(searchItemBinding: Content)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    searchItemList as ArrayList<Content>
                } else {
                    val resultList = ArrayList<Content>()
                    for (row in searchItemList) {
                        if (row.name.lowercase(Locale.ROOT)
                            .contains(charSearch.lowercase(Locale.ROOT))
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
                locationFilter = results?.values as ArrayList<Content>
                notifyDataSetChanged()
            }

        }
    }
}