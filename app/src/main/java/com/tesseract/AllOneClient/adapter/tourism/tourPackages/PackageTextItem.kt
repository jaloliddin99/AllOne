package com.tesseract.AllOneClient.adapter.tourism.tourPackages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutTourismPackageTextItemBinding
import com.tesseract.AllOneClient.model.tourism.packageView.Include

class PackageTextItem(
    private val carImageList: List<Include>,
    private val listener: OnLocationClickListener
)
    : RecyclerView.Adapter<PackageTextItem.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutTourismPackageTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : Include =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutTourismPackageTextItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(carImage: Include) {
            itemBinding.text.text=carImage.text
        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val type: Include =carImageList[adapterPosition]
            if (position!= RecyclerView.NO_POSITION){
                listener.onItemClick(type)
            }
        }

    }

    interface OnLocationClickListener{
        fun onItemClick(type: Include?)
    }
}