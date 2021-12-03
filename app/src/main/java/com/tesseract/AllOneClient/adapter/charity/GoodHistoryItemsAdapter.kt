package com.tesseract.AllOneClient.adapter.charity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutGoodDonationItemBinding
import com.tesseract.AllOneClient.model.charity.history.Charities

class GoodHistoryItemsAdapter(
    private val charities:ArrayList<Charities>
)
    : RecyclerView.Adapter<GoodHistoryItemsAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutGoodDonationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val charities : Charities =charities[position]
        holder.bind(charities)
    }

    override fun getItemCount()=charities.size

    inner class ClinicViewHolder(private val itemBinding: LayoutGoodDonationItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        fun bind(charities: Charities) {
            itemBinding.amount.text=charities.amount
            itemBinding.time.text=charities.time
            itemBinding.title.text=charities.title
        }


    }

}