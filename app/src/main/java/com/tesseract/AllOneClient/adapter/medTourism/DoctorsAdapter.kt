package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutMedTourismDoctorItemBinding
import com.tesseract.AllOneClient.model.medTourism.Doctors

class DoctorsAdapter(
    private val addBaggageImageList: List<Doctors>,
    private val listener: OnImageClickListener
)
    : RecyclerView.Adapter<DoctorsAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTourismDoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Doctors =addBaggageImageList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=addBaggageImageList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTourismDoctorItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(newsItemBinding: Doctors) {
            itemBinding.name.text=newsItemBinding.doctorName
            itemBinding.position.text=newsItemBinding.position
            itemBinding.rating.text=newsItemBinding.ranking
            itemBinding.clinicName.text=newsItemBinding.clinicName
            itemBinding.phoneNumber.text=newsItemBinding.phoneNumber
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