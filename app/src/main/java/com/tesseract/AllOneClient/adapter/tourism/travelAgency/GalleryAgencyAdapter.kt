package com.tesseract.AllOneClient.adapter.tourism.travelAgency

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutAgencyParcketsBinding

class GalleryAgencyAdapter(
    private val listener: OnChipClickListener,
    private val popularCategories:List<String>
)
    : RecyclerView.Adapter<GalleryAgencyAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutAgencyParcketsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : String =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutAgencyParcketsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: String) {
            Picasso.get().load(newsItemBinding).into(itemBinding.poster)
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: String =popularCategories[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClicked(position)
            }
        }
    }

    interface OnChipClickListener{
        fun onItemClicked(position: String)
    }
}