package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutMedTurClinicServicesBinding
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Price

class ClinicPriceAdapter (
    private val dataList:List<Price>
)
    : RecyclerView.Adapter<ClinicPriceAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurClinicServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Price =dataList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurClinicServicesBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(content: Price) {
            itemBinding.name.text=content.name
            itemBinding.price.text=content.price


        }

    }

}