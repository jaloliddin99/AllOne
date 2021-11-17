package com.tesseract.AllOneClient.adapter.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutCardsToDonateBinding
import com.tesseract.AllOneClient.databinding.LayoutHomeOrderItemsBinding
import com.tesseract.AllOneClient.model.home.HomeOrderModel

class HomeOrdersAdapter(
    var cardList:List<HomeOrderModel>,
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<HomeOrdersAdapter.AddCardItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCardItemViewHolder {
        val binding= LayoutHomeOrderItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddCardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddCardItemViewHolder, position: Int) {
        val cardListt : HomeOrderModel =cardList[position]
        holder.bind(cardListt)
    }
    override fun getItemCount()=cardList.size

    inner class AddCardItemViewHolder(private val itemBinding: LayoutHomeOrderItemsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        @SuppressLint("SetTextI18n")
        fun bind(card: HomeOrderModel) {
            itemBinding.regionImage.setImageResource(card.image)
            itemBinding.regionName.text=card.title
        }
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}