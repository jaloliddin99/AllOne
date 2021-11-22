package com.tesseract.AllOneClient.adapter.tourism.travelAgency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.tourism.hotel.HotelIndexAdapter
import com.tesseract.AllOneClient.databinding.LayoutHotelIndexBinding
import com.tesseract.AllOneClient.databinding.LayoutMedTurClinicBinding
import com.tesseract.AllOneClient.model.tourism.agency.index.Data
import java.util.*

class TravelAgencyIndexAdapter(
    private val listener: OnChipClickListener,
    private val popularCategories: MutableSet<Data>
) : RecyclerView.Adapter<TravelAgencyIndexAdapter.ClinicViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutMedTurClinicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem: Data = locationFilter.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount() = locationFilter.size
    fun addList(arrayList: MutableSet<Data>) {
        var counter = 0
        counter += itemCount
        locationFilter.addAll(arrayList)
        notifyItemRangeInserted(counter, locationFilter.size)
    }

    var locationFilter: MutableSet<Data> = HashSet()

    init {
        locationFilter = popularCategories
    }

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurClinicBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {


        fun bind(newsItemBinding: Data) {

            itemBinding.name.text = newsItemBinding.name
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
            itemBinding.rating.text = newsItemBinding.rating
            itemBinding.type.text=newsItemBinding.tour_package_cnt.toString()
            itemBinding.addr.text=newsItemBinding.location
            itemBinding.workTime.text=newsItemBinding.work_time



        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Data = locationFilter.elementAt(adapterPosition)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onChipClicked(position)
            }
        }
    }

    interface OnChipClickListener {
        fun onChipClicked(position: Data)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    popularCategories
                } else {
                    val resultList: MutableSet<Data> = HashSet()

                    for (row in popularCategories) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = popularCategories
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                locationFilter = results?.values as MutableSet<Data>
            }

        }
    }
}