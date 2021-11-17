package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutMedTurismMainBinding
import com.tesseract.AllOneClient.model.medTourism.MainMedModel

class ClinicMainAdapter(
    private val addBaggageImageList: List<MainMedModel>,
    private val listener: OnImageClickListener
)
    : RecyclerView.Adapter<ClinicMainAdapter.MedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedViewHolder {
        val binding=
            LayoutMedTurismMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MedViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MedViewHolder, position: Int) {
        val newsItem : MainMedModel =addBaggageImageList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=addBaggageImageList.size

    inner class MedViewHolder(private val itemBinding: LayoutMedTurismMainBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(newsItemBinding: MainMedModel) {
            itemBinding.clinic.text=newsItemBinding.name
            itemBinding.image.setImageResource(newsItemBinding.img)
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