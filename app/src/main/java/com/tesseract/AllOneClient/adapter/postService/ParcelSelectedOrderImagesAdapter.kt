package com.tesseract.AllOneClient.adapter.postService

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.home.RegionDriverInfoAdapter
import com.tesseract.AllOneClient.databinding.LayoutRegionDriverCarImagesBinding

class ParcelSelectedOrderImagesAdapter(
    private val carImageList: List<String>,
    private val listener: OnItemClickListener
)
    : RecyclerView.Adapter<ParcelSelectedOrderImagesAdapter.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutRegionDriverCarImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : String =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutRegionDriverCarImagesBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(carImage: String) {
            Picasso.get().load(carImage).into(itemBinding.carImage)
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

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}