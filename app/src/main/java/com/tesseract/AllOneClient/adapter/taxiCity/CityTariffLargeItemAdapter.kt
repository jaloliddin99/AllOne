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
import com.tesseract.AllOneClient.databinding.LayoutCityModalBottomDialogItemBinding
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Content

class CityTariffLargeItemAdapter(
    private val mapModelList: List<Content>,
    private val listener: OnImageClickListener,
    private val context: Context
)
    : RecyclerView.Adapter<CityTariffLargeItemAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding=
            LayoutCityModalBottomDialogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem : Content =mapModelList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=mapModelList.size

    inner class NewsItemViewHolder(private val itemBinding: LayoutCityModalBottomDialogItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        @SuppressLint("SetTextI18n")
        fun bind(model: Content) {
            Picasso.get().load(model.img).into(itemBinding.carImage)
            itemBinding.price.text=SaveData.formatPhone(model.price)+" "+context.getString(R.string.summa1)
            itemBinding.tariff.text=model.tariff
            itemBinding.timeLeft.text=model.arrival_time


            itemBinding.moreInfo.setOnClickListener {
                listener.onShowViewPager(model)
            }

        }



    }

    interface OnImageClickListener{
        fun onShowViewPager(content: Content)
    }
}