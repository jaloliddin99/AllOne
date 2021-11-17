package com.tesseract.AllOneClient.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutAllNewsItemBinding
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryDataListModel
import com.tesseract.AllOneClient.model.home.news.allNews.Data

class AllNewsAdapter(
    private val carImageList: ArrayList<Data>,
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<AllNewsAdapter.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutAllNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : Data =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    fun addList(list: ArrayList<Data>) {
        var counter=0
        counter+=itemCount
        carImageList.addAll(list)
        notifyItemRangeInserted(counter, carImageList.size)
    }

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutAllNewsItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(carImage: Data) {

            Picasso.get().load(carImage.image).into(itemBinding.newsImageView)
            itemBinding.date.text=carImage.date
            itemBinding.title.text=carImage.title

            itemBinding.mainCardView.transitionName="cardViewTransition${carImage.id}"

        }
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Data=carImageList[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClick(position, itemBinding.mainCardView)
            }
        }

    }

    interface OnItemClickListener{
        fun onItemClick(position: Data, view:MaterialCardView)
    }
}