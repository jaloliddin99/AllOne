package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutRegionDriverCarImagesBinding

class ClinicImagesAdapter(
    private val dataList:List<String>
)
    : RecyclerView.Adapter<ClinicImagesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutRegionDriverCarImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : String =dataList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutRegionDriverCarImagesBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(content: String) {
            Picasso.get().load(content).into(itemBinding.carImage)
        }

    }

}