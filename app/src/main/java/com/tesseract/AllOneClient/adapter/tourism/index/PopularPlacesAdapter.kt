package com.tesseract.AllOneClient.adapter.tourism.index

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.medTourism.ChipAdapter
import com.tesseract.AllOneClient.databinding.LayoutTextviewChipBinding
import com.tesseract.AllOneClient.model.tourism.main.index.PopularPlace

class PopularPlacesAdapter(
    private val listener: OnChipClickListener,
    private val popularCategories:List<PopularPlace>
)
    : RecyclerView.Adapter<PopularPlacesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutTextviewChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : PopularPlace =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutTextviewChipBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: PopularPlace) {
            itemBinding.title.text=newsItemBinding.name
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: PopularPlace =popularCategories[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }
    }

    interface OnChipClickListener{
        fun onChipClicked(position: PopularPlace)
    }
}