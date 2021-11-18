package com.tesseract.AllOneClient.adapter.medTourism.doctors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMedTurClinicBinding
import com.tesseract.AllOneClient.model.medTourism.doctorView.Clinic

class DoctorsClinicAdapter(
    private val arrayList: List<Clinic>,
    private val listener: OnClickListener
)
    : RecyclerView.Adapter<DoctorsClinicAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurClinicBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Clinic =arrayList.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount()=arrayList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurClinicBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(data: Clinic) {
            itemBinding.rating.text=data.rating
            itemBinding.addr.text=data.addr
            itemBinding.name.text=data.name
            itemBinding.type.text=data.type
            itemBinding.workTime.text=data.work_time
            Picasso.get().load(data.poster).into(itemBinding.poster)
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Clinic=arrayList.elementAt(adapterPosition)
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnClickListener{
        fun onChipClicked(position: Clinic)
    }
}