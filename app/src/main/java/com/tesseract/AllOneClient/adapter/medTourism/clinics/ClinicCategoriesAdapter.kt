package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutClinicDropdownItemBinding
import com.tesseract.AllOneClient.model.medTourism.categories.Content

class ClinicCategoriesAdapter(
    private val dataList:List<Content>,
    private val listener: CategoriesClickListener
)
    : RecyclerView.Adapter<ClinicCategoriesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutClinicDropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Content =dataList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutClinicDropdownItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(content: Content) {
            itemBinding.textView.text=content.name
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Content=dataList[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface CategoriesClickListener{
        fun onChipClicked(position: Content)
    }
}