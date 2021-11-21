package com.tesseract.AllOneClient.adapter.tourism.carRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutCarrItemListBinding
import com.tesseract.AllOneClient.model.tourism.carRent.cars.Data
import java.util.*

class CarRentCarsAdapter(
    private val listener: OnChipClickListener,
    private val popularCategories:MutableSet<Data>
)
    : RecyclerView.Adapter<CarRentCarsAdapter.ClinicViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutCarrItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }


    var locationFilter: MutableSet<Data> = HashSet()


    init {
        locationFilter = popularCategories
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Data =popularCategories.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount()=locationFilter.size

    inner class ClinicViewHolder(private val itemBinding: LayoutCarrItemListBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: Data) {
            itemBinding.name.text=newsItemBinding.name
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
            itemBinding.addr.text=newsItemBinding.addr
            itemBinding.company.text=newsItemBinding.company
            itemBinding.price.text=newsItemBinding.price

        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Data =locationFilter.elementAt(adapterPosition)
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.carSelected(position)
            }
        }
    }

    interface OnChipClickListener{
        fun carSelected(position: Data)
    }

    fun addList(list: Set<Data>) {
        var counter=0
        counter += itemCount
        locationFilter.addAll(list)
        notifyItemRangeInserted(counter, locationFilter.size)
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