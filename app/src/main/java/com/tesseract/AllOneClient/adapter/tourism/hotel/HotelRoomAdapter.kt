package com.tesseract.AllOneClient.adapter.tourism.hotel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutHotelRoomItemBinding
import com.tesseract.AllOneClient.model.tourism.hotels.hotelView.HotelRoom

class HotelRoomAdapter(
    private val listener: OnChipClickListener,
    private val popularCategories:List<HotelRoom>
)
    : RecyclerView.Adapter<HotelRoomAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutHotelRoomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : HotelRoom =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutHotelRoomItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: HotelRoom) {
            itemBinding.name.text=newsItemBinding.name
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
            itemBinding.price.text=newsItemBinding.price
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: HotelRoom =popularCategories[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClicked(position)
            }
        }
    }

    interface OnChipClickListener{
        fun onItemClicked(position: HotelRoom)
    }
}