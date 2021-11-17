package com.tesseract.AllOneClient.adapter.taxiCity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutCityCargoItemsBinding
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Opt

class CityTariffCargoItems(
    private val mapModelList: List<Opt>,
    private val listener: OnCargoSelectListener
)
    : RecyclerView.Adapter<CityTariffCargoItems.NewsItemViewHolder>() {
    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding=
            LayoutCityCargoItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem : Opt =mapModelList[position]
        holder.bind(newsItem)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount()=mapModelList.size

    inner class NewsItemViewHolder(private val itemBinding: LayoutCityCargoItemsBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        @SuppressLint("SetTextI18n")
        fun bind(model: Opt) {
            if (adapterPosition==mapModelList.size-1){
                itemBinding.line.visibility= View.GONE
            }else{
                itemBinding.line.visibility=View.VISIBLE
            }
            itemBinding.type.text=model.title
            itemBinding.description.text=model.subtitle
            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                itemBinding.checkbox.isChecked=true
                listener.onCargoSelect(model)
            } else {
                itemView.isSelected = false
                itemBinding.checkbox.isChecked=false
            }
        }

    }

    interface OnCargoSelectListener{
        fun onCargoSelect(opt: Opt)
    }
}