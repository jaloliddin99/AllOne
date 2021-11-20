package com.tesseract.AllOneClient.adapter.tourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutTourExploreBinding
import com.tesseract.AllOneClient.model.tourism.explore.Content
import com.tesseract.AllOneClient.model.tourism.indexUzb.Data
import java.util.*
import kotlin.collections.ArrayList

class ExplorePageAdapter(
    private val listener: OnExploreListener,
    private val popularCategories: List<Content>
) : RecyclerView.Adapter<ExplorePageAdapter.ClinicViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutTourExploreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem: Content = locationFilter[position]
        holder.bind(newsItem)
    }

    var locationFilter: List<Content>


    init {
        locationFilter = popularCategories
    }

    override fun getItemCount() = locationFilter.size

    inner class ClinicViewHolder(private val itemBinding: LayoutTourExploreBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {


        fun bind(element: Content) {
            Picasso.get().load(element.poster).into(itemBinding.image)
            itemBinding.name.text=element.name
            itemBinding.tourPackages.text=element.tour_packages.toString()
        }

        init {
            locationFilter = popularCategories
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Content = popularCategories[adapterPosition]
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onExploreListener(position)
            }
        }
    }

    interface OnExploreListener {
        fun onExploreListener(position: Content)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    popularCategories
                } else {
                    val resultList= ArrayList<Content>()

                    for (row in popularCategories) {
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))
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
                locationFilter = results?.values as List<Content>
                notifyDataSetChanged()
            }

        }
    }
}