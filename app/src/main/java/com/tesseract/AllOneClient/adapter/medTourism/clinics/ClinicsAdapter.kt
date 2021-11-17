package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMedTurClinicBinding
import com.tesseract.AllOneClient.model.medTourism.clinics.Data
import java.util.*
import kotlin.collections.ArrayList

class ClinicsAdapter(
    private val arrayList: MutableSet<Data>,
    private val listener: OnClickListener
)
    : RecyclerView.Adapter<ClinicsAdapter.ClinicViewHolder>(), Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurClinicBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    var locationFilter: MutableSet<Data> = HashSet()


    init {
        locationFilter = arrayList
    }


    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Data =locationFilter.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount()=locationFilter.size

    fun addList(list: Set<Data>) {
        var counter=0
        counter += itemCount
        locationFilter.addAll(list)
        notifyItemRangeInserted(counter, locationFilter.size)
    }

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurClinicBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(data: Data) {
            itemBinding.rating.text=data.rating
            itemBinding.addr.text=data.addr
            itemBinding.name.text=data.name
            itemBinding.type.text=data.type
            itemBinding.workTime.text=data.work_time
            Picasso.get().load(data.poster).into(itemBinding.poster)
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=locationFilter.elementAt(adapterPosition).id
            if (position!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnClickListener{
        fun onChipClicked(position: Int)
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