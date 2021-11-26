package com.tesseract.AllOneClient.adapter.taxiCity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutCancelOrderBinding
import com.tesseract.AllOneClient.databinding.LayoutCommentaryItemBinding
import com.tesseract.AllOneClient.model.taxiCity.CancelOrder.Content

class AddCommentAdapter(
    private val listener: CancelOrderListener
)
    : RecyclerView.Adapter<AddCommentAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder {
        val binding=
            LayoutCommentaryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewsItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
//        val newsItem : Content =mapModelList[position]
        holder.bind()
    }

    override fun getItemCount()=0

    inner class NewsItemViewHolder(private val itemBinding: LayoutCommentaryItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind() {
            listener.onItemClick(itemBinding.txtComment.text.toString())
        }

    }

    interface CancelOrderListener{
        fun onItemClick(reason: String)
    }
}