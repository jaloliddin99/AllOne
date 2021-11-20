package com.tesseract.AllOneClient.adapter.tourism.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.TourPackagesAdapter
import com.tesseract.AllOneClient.databinding.LayoutTourpacketItemBinding
import com.tesseract.AllOneClient.model.tourism.expCountryPackage.Data
import java.util.*

class ExploreCPAdapter(
    private val listener: OnExploreListener,
    private val arrayList: MutableSet<Data>
) : RecyclerView.Adapter<ExploreCPAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutTourpacketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem: Data = arrayList.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount() = arrayList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutTourpacketItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {


        fun bind(element: Data) {
            Picasso.get().load(element.poster).into(itemBinding.poster)
            itemBinding.location.text=element.location
            itemBinding.name.text=element.name
            itemBinding.priceFrom.text=element.price_from
            itemBinding.rating.text=element.rating

            itemBinding.constraintLayout.visibility=View.GONE

        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Data = arrayList.elementAt(adapterPosition)
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onExploreListener(position)
            }
        }
    }

    fun addList(list: Set<Data>) {
        var counter=0
        counter += itemCount
        arrayList.addAll(list)
        notifyItemRangeInserted(counter, arrayList.size)
    }

    interface OnExploreListener {
        fun onExploreListener(position: Data)
    }

}