package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutCityContactBinding
import com.tesseract.AllOneClient.databinding.LayoutCityItemBinding
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.taxiCity.ContactModel
import java.util.*
import kotlin.collections.ArrayList

class ContactAdapter(
    private val mapModelList: ArrayList<ContactModel>,
    private val onSelectedContact: OnContactSelected
) : RecyclerView.Adapter<ContactAdapter.NewsItemViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding =
            LayoutCityContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }
    var locationFilter = ArrayList<ContactModel>()
    private var selectedPosition = -1
    init {
        locationFilter = mapModelList
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem: ContactModel = locationFilter[position]
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

        fun bind(model: ContactModel) {
            itemBinding.name.text = model.name
            itemBinding.phone.text=model.phone

            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                itemBinding.radio.isChecked=true
                onSelectedContact.onSelect(model)

            } else {
                itemView.isSelected = false
                itemBinding.radio.isChecked=false
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
                    val resultList = ArrayList<ContactModel>()
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
                locationFilter = results?.values as ArrayList<ContactModel>
            }

        }
    }

    interface OnContactSelected {
        fun onSelect(contact: ContactModel)
    }
}