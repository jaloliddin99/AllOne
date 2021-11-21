package com.tesseract.AllOneClient.adapter.tourism.carRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutCarRentPopularBrendBinding
import com.tesseract.AllOneClient.databinding.LayoutGreenChipBinding
import com.tesseract.AllOneClient.model.tourism.countries.Content

class CarRentSelectedItems (
    private val popularCategories:ArrayList<Content>
)
    : RecyclerView.Adapter<CarRentSelectedItems.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutGreenChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Content =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutGreenChipBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        fun bind(newsItemBinding: Content) {
            itemBinding.name.text=newsItemBinding.name

            itemBinding.cancel.setOnClickListener {
                popularCategories.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

    }

    interface OnChipClickListener{
        fun onChipClicked(position: Content)
    }
}