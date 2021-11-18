package com.tesseract.AllOneClient.adapter.medTourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutFavouriteItemBinding
import com.tesseract.AllOneClient.databinding.LayoutMedPhoneItemsBinding
import com.tesseract.AllOneClient.model.medTourism.favourites.Data

class FavouritesAdapter (
    private val arrayList: MutableSet<Data>,
    private val listener: OnClickListener,
    private val removeListener:OnRemoveListener
)
    : RecyclerView.Adapter<FavouritesAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutFavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ClinicViewHolder(binding)

    }
    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Data =arrayList.elementAt(position)
        holder.bind(newsItem)
    }
    fun addList(list: Set<Data>) {
        var counter=0
        counter += itemCount
        arrayList.addAll(list)
        notifyItemRangeInserted(counter, arrayList.size)
    }
    fun removedItem(data: Data, position: Int){
        arrayList.remove(data)
        notifyItemRemoved(position)
    }

    override fun getItemCount()=arrayList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutFavouriteItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(data: Data) {

            Picasso.get().load(data.poster).into(itemBinding.poster)
            itemBinding.subtitle.text=data.subtitle
            itemBinding.title.text=data.title

            itemBinding.remove.setOnClickListener {
                removeListener.removeRequest(data, adapterPosition)
            }

        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Data =arrayList.elementAt(adapterPosition)
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onChipClicked(position)
            }
        }

    }

    interface OnClickListener{
        fun onChipClicked(position: Data)
    }

    interface OnRemoveListener{
        fun removeRequest(data: Data, position: Int)
    }


}