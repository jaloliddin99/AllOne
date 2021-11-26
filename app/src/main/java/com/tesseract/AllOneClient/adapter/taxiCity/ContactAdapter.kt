package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutCityContactBinding
import com.tesseract.AllOneClient.model.taxiCity.Contact
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private val mapModelList: ArrayList<Contact>,
    private val onSelectedContact: OnContactSelected
) : RecyclerView.Adapter<ContactAdapter.NewsItemViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding =
            LayoutCityContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }
    var locationFilter = ArrayList<Contact>()
    private var selectedPosition = -1
    init {
        locationFilter = mapModelList
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem: Contact = locationFilter[position]
        holder.bind(newsItem)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount() = locationFilter.size

    inner class NewsItemViewHolder(private val itemBinding: LayoutCityContactBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(model: Contact) {
            itemBinding.name.text = model.name
            if (model.numbers.size>0){
                itemBinding.phone.text=model.numbers[0]
            }

            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                itemBinding.radio.setImageResource(R.drawable.ic_check_box)
                onSelectedContact.onSelect(model)

            } else {
                itemView.isSelected = false
                itemBinding.radio.setImageResource(R.drawable.ic_unchecked_radio_button)
            }

        }
        init {
            locationFilter = mapModelList
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    mapModelList
                } else {
                    val resultList = ArrayList<Contact>()
                    for (row in mapModelList) {
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
                locationFilter = results?.values as ArrayList<Contact>
                notifyDataSetChanged()
            }

        }
    }

    interface OnContactSelected {
        fun onSelect(contact: Contact)
    }
}