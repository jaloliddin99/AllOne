package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMedTurizmPopularClinicsBinding
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularClinic

class PopularAdapter (
    private val listener: PopularClinics,
    private val list:List<PopularClinic>
)
    : RecyclerView.Adapter<PopularAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutMedTurizmPopularClinicsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : PopularClinic =list[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=list.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMedTurizmPopularClinicsBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: PopularClinic) {
            itemBinding.addr.text=newsItemBinding.addr
            itemBinding.name.text=newsItemBinding.name
            Picasso.get().load(newsItemBinding.poster).into(itemBinding.poster)
            itemBinding.rating.text=newsItemBinding.rating
            itemBinding.reviewCount.text="(${newsItemBinding.review_count.toString()})"
            itemBinding.workTime.text=newsItemBinding.work_time
            itemBinding.type.text=newsItemBinding.type
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:PopularClinic=list[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onPopularClicked(position)
            }
        }

    }

    interface PopularClinics{
        fun onPopularClicked(position: PopularClinic)
    }
}