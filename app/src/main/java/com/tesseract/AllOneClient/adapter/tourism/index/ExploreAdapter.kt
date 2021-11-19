package com.tesseract.AllOneClient.adapter.tourism.index

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutTextviewChipBinding
import com.tesseract.AllOneClient.databinding.LayoutTourismDiscoverBinding
import com.tesseract.AllOneClient.model.tourism.main.index.ExploreCountry

class ExploreAdapter(
    private val listener: OnExploreListener,
    private val popularCategories: List<ExploreCountry>
) : RecyclerView.Adapter<ExploreAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutTourismDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem: ExploreCountry = popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount() = popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutTourismDiscoverBinding) :
        RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {


        fun bind(element: ExploreCountry) {
            Picasso.get().load(element.poster).into(itemBinding.image)
            itemBinding.name.text=element.name
            itemBinding.tourPackages.text=element.tour_packages.toString()
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: ExploreCountry = popularCategories[adapterPosition]
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onExploreListener(position)
            }
        }
    }

    interface OnExploreListener {
        fun onExploreListener(position: ExploreCountry)
    }
}