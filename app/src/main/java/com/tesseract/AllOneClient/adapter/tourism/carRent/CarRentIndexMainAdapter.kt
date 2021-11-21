package com.tesseract.AllOneClient.adapter.tourism.carRent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutCarRentCarBinding
import com.tesseract.AllOneClient.databinding.LayoutCarRentPopularBrendBinding
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.CarType

class CarRentIndexMainAdapter  (
    private val listener: OnChipClickListener,
    private val popularCategories:List<CarType>,
    private val context: Context
)
    : RecyclerView.Adapter<CarRentIndexMainAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutCarRentCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : CarType =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutCarRentCarBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        fun bind(element: CarType) {

            itemBinding.name.text=element.type
            itemBinding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.recyclerView.adapter=CarRentIndexMainItemAdapter(element.cars)


            itemBinding.all.setOnClickListener {
                listener.onItemClicked(element)
            }

        }
    }

    interface OnChipClickListener{
        fun onItemClicked(position: CarType)
    }
}