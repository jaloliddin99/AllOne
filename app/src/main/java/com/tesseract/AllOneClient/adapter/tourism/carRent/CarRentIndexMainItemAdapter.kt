package com.tesseract.AllOneClient.adapter.tourism.carRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutCarRentCarItemsBinding
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.Car
import com.tesseract.AllOneClient.model.order.MessageEvent

import org.greenrobot.eventbus.EventBus




class CarRentIndexMainItemAdapter  (
    private val popularCategories:List<Car>
)
    : RecyclerView.Adapter<CarRentIndexMainItemAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutCarRentCarItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Car =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutCarRentCarItemsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: Car) {
            itemBinding.name.text=newsItemBinding.name
            itemBinding.price.text=newsItemBinding.price
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            EventBus.getDefault().post(popularCategories[adapterPosition])
        }

    }
}