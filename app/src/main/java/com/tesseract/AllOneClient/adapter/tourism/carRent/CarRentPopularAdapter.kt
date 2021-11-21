package com.tesseract.AllOneClient.adapter.tourism.carRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutCarRentPopularBrendBinding
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.PopularBrand

class CarRentPopularAdapter (
    private val listener: OnChipClickListener,
    private val popularCategories:List<PopularBrand>
)
    : RecyclerView.Adapter<CarRentPopularAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutCarRentPopularBrendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : PopularBrand =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutCarRentPopularBrendBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: PopularBrand) {
            itemBinding.name.text=newsItemBinding.name
            Picasso.get().load(newsItemBinding.logo).into(itemBinding.poster)
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: PopularBrand =popularCategories[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }
    }

    interface OnChipClickListener{
        fun onChipClicked(position: PopularBrand)
    }
}