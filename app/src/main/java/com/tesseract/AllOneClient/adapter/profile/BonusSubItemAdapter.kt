package com.tesseract.AllOneClient.adapter.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutBonuSubItemBinding
import com.tesseract.AllOneClient.model.profile.bonus.Bonuse

class BonusSubItemAdapter (
    private val charities:ArrayList<Bonuse>
)
    : RecyclerView.Adapter<BonusSubItemAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutBonuSubItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val charities : Bonuse =charities[position]
        holder.bind(charities)
    }

    override fun getItemCount()=charities.size

    inner class ClinicViewHolder(private val itemBinding: LayoutBonuSubItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        fun bind(charities: Bonuse) {
            itemBinding.amount.text=charities.amount
            itemBinding.time.text=charities.time
            itemBinding.title.text=charities.title
        }


    }

}