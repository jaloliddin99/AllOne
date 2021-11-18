package com.tesseract.AllOneClient.adapter.medTourism

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMedTurizmPopulardoctorsBinding
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularDoctor

class PopularDoctorAdapter(
    private val listener: OnDoctorClicked,
    private val list:List<PopularDoctor>
)
    : RecyclerView.Adapter<PopularDoctorAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurizmPopulardoctorsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : PopularDoctor =list[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=list.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurizmPopulardoctorsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        @SuppressLint("SetTextI18n")
        fun bind(newsItemBinding: PopularDoctor) {
            itemBinding.name.text=newsItemBinding.name
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
            itemBinding.rating.text=newsItemBinding.rating
            itemBinding.reviewCount.text="(${newsItemBinding.review_count})"
            itemBinding.profession.text=newsItemBinding.profession
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:PopularDoctor=list[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnDoctorClicked{
        fun onChipClicked(position: PopularDoctor)
    }
}