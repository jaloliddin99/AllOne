package com.tesseract.AllOneClient.adapter.tourism.tourPackages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutTourismPackageTextItemBinding
import com.tesseract.AllOneClient.model.tourism.packageView.ExtraPaid
import com.tesseract.AllOneClient.model.tourism.packageView.Include

class ExtraPaidAdapter(
    private val carImageList: List<ExtraPaid>
)
    : RecyclerView.Adapter<ExtraPaidAdapter.RegionDriverInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RegionDriverInfoViewHolder {
        val binding=
            LayoutTourismPackageTextItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RegionDriverInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RegionDriverInfoViewHolder, position: Int) {
        val newsItem : ExtraPaid =carImageList[position]
        holder.bind(newsItem)
    }
    override fun getItemCount()=carImageList.size

    inner class RegionDriverInfoViewHolder(private val itemBinding: LayoutTourismPackageTextItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(carImage: ExtraPaid) {
            itemBinding.text.text=carImage.text
        }


    }

}