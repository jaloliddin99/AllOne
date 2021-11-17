package com.tesseract.AllOneClient.adapter.tourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutTourPacketItemBinding
import com.tesseract.AllOneClient.model.tourism.TourPacketsModel

class TourPacketAdapter(
    private val addBaggageImageList: List<TourPacketsModel>,
    private val listener: OnImageClickListener
)
    : RecyclerView.Adapter<TourPacketAdapter.TourPacketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourPacketViewHolder {
        val binding=
            LayoutTourPacketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TourPacketViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TourPacketViewHolder, position: Int) {
        val newsItem : TourPacketsModel =addBaggageImageList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=addBaggageImageList.size

    inner class TourPacketViewHolder(private val itemBinding: LayoutTourPacketItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(newsItemBinding: TourPacketsModel) {
            itemBinding.image.setImageResource(newsItemBinding.image)
            itemBinding.description.text=newsItemBinding.description
            itemBinding.extraDescription.text=newsItemBinding.extraDescription
            itemBinding.money.text=newsItemBinding.money
            itemBinding.period.text=newsItemBinding.period
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (position!= RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

    }

    interface OnImageClickListener{
        fun onItemClick(position: Int)
    }
}