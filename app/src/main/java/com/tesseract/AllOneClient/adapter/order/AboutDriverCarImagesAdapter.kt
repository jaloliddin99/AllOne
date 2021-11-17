package com.tesseract.AllOneClient.adapter.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.databinding.LayoutCardCarPhotoBinding

class AboutDriverCarImagesAdapter(
    private val carImageList: List<String>
)
    : RecyclerView.Adapter<AboutDriverCarImagesAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=
            LayoutCardCarPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newsItem : String =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class MyViewHolder(private val itemBinding: LayoutCardCarPhotoBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(newsItemBinding: String) {
            Glide.with(itemView).load(newsItemBinding).into( itemBinding.carImage)
        }
    }

}