package com.tesseract.AllOneClient.adapter.tourism.travelAgency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.TourPackagesAdapter
import com.tesseract.AllOneClient.databinding.LayoutAgencyTourpacketBinding
import com.tesseract.AllOneClient.databinding.LayoutTourpacketItemBinding
import com.tesseract.AllOneClient.model.tourism.agency.packageView.Data
import java.util.*

class AgencyPackagesAdapter (
    private val listener: OnExploreListener,
    private val arrayList: MutableSet<Data>
) : RecyclerView.Adapter<AgencyPackagesAdapter.ClinicViewHolder>() , Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutAgencyTourpacketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }
    var locationFilter: MutableSet<Data> = HashSet()


    init {
        locationFilter = arrayList
    }
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem: Data = locationFilter.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount() = locationFilter.size

    inner class ClinicViewHolder(private val itemBinding: LayoutAgencyTourpacketBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {


        fun bind(element: Data) {
            Picasso.get().load(element.poster).into(itemBinding.poster)
            itemBinding.location.text=element.location
            itemBinding.name.text=element.name
            itemBinding.priceFrom.text=element.price_from
            itemBinding.rating.text=element.rating


        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Data = locationFilter.elementAt(adapterPosition)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onExploreListener(position)
            }
        }
    }

    fun addList(list: Set<Data>) {
        var counter=0
        counter += itemCount
        locationFilter.addAll(list)
        notifyItemRangeInserted(counter, locationFilter.size)
    }

    interface OnExploreListener {
        fun onExploreListener(position: Data)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    arrayList
                } else {
                    val resultList: MutableSet<Data> = HashSet()

                    for (row in arrayList) {
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
                locationFilter = results?.values as MutableSet<Data>
            }

        }
    }
}