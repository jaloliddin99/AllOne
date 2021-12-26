package com.tesseract.AllOneClient.adapter.medTourism.amb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicCategoriesAdapter
import com.tesseract.AllOneClient.constants.SaveData.formatPhone
import com.tesseract.AllOneClient.databinding.LayoutClinicDropdownItemBinding
import com.tesseract.AllOneClient.databinding.LayoutMedAmbulanceBinding
import com.tesseract.AllOneClient.model.medTourism.ambulance.Content
import java.util.*
import kotlin.collections.ArrayList

class AmbulanceAdapter(
    private val dataList:List<Content>,
    private val listener: CategoriesClickListener,
    private val context: Context
)
    : RecyclerView.Adapter<AmbulanceAdapter.ClinicViewHolder>(), Filterable {

    var locationFilter = ArrayList<Content>()

    init {
        locationFilter = dataList as ArrayList<Content>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedAmbulanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Content =locationFilter[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=locationFilter.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedAmbulanceBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(content: Content) {
            itemBinding.call.setOnClickListener {
                listener.onChipClicked(content)
            }
            itemBinding.name.text=content.name
            itemBinding.phoneNumber.text=content.phone_number
            itemBinding.price.text= formatPhone(content.price)+" "+context.getString(R.string.summa1)



            Picasso.get().load(content.poster).into(itemBinding.poster)
        }

    }

    interface CategoriesClickListener{
        fun onChipClicked(position: Content)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                locationFilter = if (charSearch.isEmpty()) {
                    dataList as ArrayList<Content>
                } else {
                    val resultList = ArrayList<Content>()
                    for (row in dataList) {
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