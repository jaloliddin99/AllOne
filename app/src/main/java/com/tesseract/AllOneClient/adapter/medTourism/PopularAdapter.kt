package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutMedTurizmPopularClinicsBinding

class PopularAdapter (
    private val listener: OnChipClickListener
)
    : RecyclerView.Adapter<PopularAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurizmPopularClinicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
//        val newsItem : MedClinicModel =addBaggageImageList[position]
//        holder.bind(newsItem)
    }

    override fun getItemCount()=8

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurizmPopularClinicsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


//        fun bind(newsItemBinding: MedClinicModel) {
//            itemBinding.title.text=newsItemBinding.title
//            itemBinding.description.text=newsItemBinding.description
//            itemBinding.image.setImageResource(newsItemBinding.img)
//        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (position!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnChipClickListener{
        fun onChipClicked(position: Int)
    }
}