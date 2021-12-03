package com.tesseract.AllOneClient.adapter.charity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.databinding.LayoutMakeDonationItemBinding
import com.tesseract.AllOneClient.model.charity.projects.Data

class MakeDonationAdapter(
    private val onClickAction: OnItemCLicked,
    private val dataList: MutableSet<Data>
) : RecyclerView.Adapter<MakeDonationAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding =
            LayoutMakeDonationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ClinicViewHolder(binding)

    }
    fun addList(list: MutableSet<Data>) {
        var counter=0
        counter+=itemCount
        dataList.addAll(list)
        notifyItemRangeInserted(counter, dataList.size)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val newsItem : Data =dataList.elementAt(position)
        holder.bind(newsItem)
    }

    override fun getItemCount() = dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutMakeDonationItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        fun bind(data:Data) {
            itemBinding.title.text=data.title
            Picasso.get().load(data.image).into(itemBinding.image)


            itemView.setOnClickListener {
                onClickAction.onItemCLicked(data.id)
            }

        }


    }

    interface OnItemCLicked {
        fun onItemCLicked(position: Int)
    }

}