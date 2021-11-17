package com.tesseract.AllOneClient.adapter.medTourism.amb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicCategoriesAdapter
import com.tesseract.AllOneClient.databinding.LayoutClinicDropdownItemBinding
import com.tesseract.AllOneClient.databinding.LayoutMedAmbulanceBinding
import com.tesseract.AllOneClient.model.medTourism.ambulance.Content

class AmbulanceAdapter(
    private val dataList:List<Content>,
    private val listener: CategoriesClickListener
)
    : RecyclerView.Adapter<AmbulanceAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedAmbulanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Content =dataList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedAmbulanceBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(content: Content) {
            itemBinding.call.setOnClickListener {
                listener.onChipClicked(content)
            }
            itemBinding.name.text=content.name
            itemBinding.phoneNumber.text=content.phone_number
            itemBinding.price.text=content.price

            Picasso.get().load(content.poster).into(itemBinding.poster)
        }


    }

    interface CategoriesClickListener{
        fun onChipClicked(position: Content)
    }
}