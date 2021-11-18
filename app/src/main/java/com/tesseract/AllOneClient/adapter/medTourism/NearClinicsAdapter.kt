package com.tesseract.AllOneClient.adapter.medTourism

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMedTurizmNearClinicsBinding
import com.tesseract.AllOneClient.model.medTourism.medMain.NearbyClinic
import dagger.hilt.android.AndroidEntryPoint

class NearClinicsAdapter (
    private val listener: OnNearByKlicked,
    private val list: List<NearbyClinic>
)
    : RecyclerView.Adapter<NearClinicsAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurizmNearClinicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : NearbyClinic =list[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=list.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurizmNearClinicsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        @SuppressLint("SetTextI18n")
        fun bind(item: NearbyClinic) {


            itemBinding.addr.text=item.addr
            itemBinding.distance.text="${item.distance} km"
            itemBinding.name.text=item.name
            Picasso.get().load(item.poster).into(itemBinding.poster)

        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:NearbyClinic=list[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnNearByKlicked{
        fun onChipClicked(position: NearbyClinic)
    }
}