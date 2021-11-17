package com.tesseract.AllOneClient.adapter.taxiCity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.LayoutCityCarListaBinding
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Content

class CityTariffAdapter(
    private val markPosition: Int,
    private val mapModelList: List<Content>,
    private val listener: OnImageClickListener,
    private val context: Context
) : RecyclerView.Adapter<CityTariffAdapter.NewsItemViewHolder>() {
    private var selectedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding =
            LayoutCityCarListaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem: Content = mapModelList[position]
        holder.bind(newsItem, markPosition)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            notifyItemChanged(markPosition)
        }
    }

    override fun getItemCount() = mapModelList.size

    companion object{
        var isMainSelected = true
    }

    inner class NewsItemViewHolder(private val itemBinding: LayoutCityCarListaBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(model: Content, markPosition: Int) {
            Picasso.get().load(model.icon).into(itemBinding.imageCar)
            itemBinding.price.text =
                SaveData.formatPhone(model.price) + context.getString(R.string.summa1)
            itemBinding.type.text = model.title

            if (selectedPosition == adapterPosition) {

                isMainSelected = false
                itemView.isSelected = true

                itemBinding.mainBg.setBackgroundResource(R.drawable.bg_item_clicked_round_yellow)
                listener.onItemClick(model, adapterPosition)
            } else {
                itemView.isSelected = false
                itemBinding.mainBg.setBackgroundResource(R.drawable.bg_item_not_clicked_8dp)
            }

//            if (mapModelList.lastIndex==adapterPosition){
//                if (!isMainSelected){
//                    itemBinding.mainBg.setBackgroundResource(R.drawable.bg_item_not_clicked_8dp)
//                }
//            }
            if (markPosition == adapterPosition&&isMainSelected) {
                itemBinding.mainBg.setBackgroundResource(R.drawable.bg_item_clicked_round_yellow)
                listener.onItemClick(model, adapterPosition)
            }
        }

    }

    interface OnImageClickListener {
        fun onItemClick(content: Content, position: Int)
    }
}