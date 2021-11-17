package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutMedTurServicesItemBinding
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Service

class ClinicsServicesAdapter (
    private val dataList:List<Service>,
    private val listener: CategoriesClickListener
)
    : RecyclerView.Adapter<ClinicsServicesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurServicesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Service =dataList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurServicesItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(service: Service) {
            itemBinding.description.text=service.description
            itemBinding.name.text=service.name
            itemBinding.price.text=service.price

        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Service =dataList[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface CategoriesClickListener{
        fun onChipClicked(position: Service)
    }
}