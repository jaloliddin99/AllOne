package com.tesseract.AllOneClient.adapter.tourism.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.medTourism.ChipAdapter
import com.tesseract.AllOneClient.databinding.LayoutClinicDropdownItemBinding
import com.tesseract.AllOneClient.databinding.LayoutTextviewChipBinding
import com.tesseract.AllOneClient.model.tourism.countries.Content

class CurrenciesAdapter (
    private val listener: OnChipClickListener,
    private val popularCategories:List<Content>
)
    : RecyclerView.Adapter<CurrenciesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutClinicDropdownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Content =popularCategories[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=popularCategories.size

    inner class ClinicViewHolder(private val itemBinding: LayoutClinicDropdownItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        fun bind(newsItemBinding: Content) {
            itemBinding.textView.text=newsItemBinding.name
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Content =popularCategories[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }
    }

    interface OnChipClickListener{
        fun onChipClicked(position: Content)
    }
}