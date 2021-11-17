package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutCancelOrderBinding
import com.tesseract.AllOneClient.model.taxiCity.CancelOrder.CancelOrder
import com.tesseract.AllOneClient.model.taxiCity.CancelOrder.Content

class CancelOrderAdapter(

    private val mapModelList: List<Content>,
    private val listener: CancelOrderListener
)
    : RecyclerView.Adapter<CancelOrderAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding=
            LayoutCancelOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem : Content =mapModelList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount()=mapModelList.size

    inner class NewsItemViewHolder(private val itemBinding: LayoutCancelOrderBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(model: Content) {
            itemBinding.checkbox.text=model.text

            itemBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    reason += "${model.id},"
                }else{
                    reason=reason.replace("${model.id},", "")
                }
                listener.onItemClick(reason)
            }

        }

    }
    private var reason=""


    interface CancelOrderListener{
        fun onItemClick(reason: String)
    }
}