package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.medTourism.doctors.DoctorsClinicAdapter
import com.tesseract.AllOneClient.databinding.LayoutMedPhoneItemsBinding
import com.tesseract.AllOneClient.databinding.LayoutMedTurClinicBinding
import com.tesseract.AllOneClient.model.medTourism.doctorView.Clinic

class MedPhoneAdapter(
    private val arrayList: List<String>,
    private val listener: OnClickListener
)
    : RecyclerView.Adapter<MedPhoneAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedPhoneItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : String =arrayList.elementAt(position)
        holder.bind(newsItem)
    }


    override fun getItemCount()=arrayList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedPhoneItemsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(data: String) {
            itemBinding.phoneNumber.text=data
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: String =arrayList.elementAt(adapterPosition)
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnClickListener{
        fun onChipClicked(position: String)
    }


}