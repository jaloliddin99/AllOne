package com.tesseract.AllOneClient.adapter.charity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutGoodDonationItemBinding
import com.tesseract.AllOneClient.model.charity.history.Order

class GoodHistoryItemsAdapter(
    private val orders:ArrayList<Order>
)
    : RecyclerView.Adapter<GoodHistoryItemsAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutGoodDonationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val order : Order =orders[position]
        holder.bind(order)
    }

    override fun getItemCount()=orders.size

    inner class ClinicViewHolder(private val itemBinding: LayoutGoodDonationItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        fun bind(order: Order) {
            itemBinding.amount.text=order.amount
            itemBinding.time.text=order.time
            itemBinding.title.text=order.title
        }


    }

}