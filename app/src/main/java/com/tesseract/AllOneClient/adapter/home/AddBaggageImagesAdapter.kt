package com.tesseract.AllOneClient.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.databinding.LayoutBaggageImagesBinding
import com.tesseract.AllOneClient.databinding.LayoutRecyclerviewImageBinding
import kotlinx.android.synthetic.main.layout_baggage_images.view.*

class AddBaggageImagesAdapter(
    private val addBaggageImageList: ArrayList<Any>,
    private val listener: OnImageClickListener
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ADD = 0
    private val DELETE = 1

    override fun getItemViewType(position: Int): Int {
        return if(position==0){
            ADD
        }else{
            DELETE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType:
    Int): RecyclerView.ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null

        viewHolder = if (viewType == ADD) {
            val binding=
                LayoutRecyclerviewImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddViewHolder(binding)
        } else {
            val binding=
                LayoutBaggageImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DeleteViewHolder(binding)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is AddViewHolder){
            holder.parcelBind(addBaggageImageList[position])
        }
        if (holder is DeleteViewHolder) {
            holder.bind(addBaggageImageList[position])
        }
    }

    override fun getItemCount()=addBaggageImageList.size

    inner class AddViewHolder(private val itemBinding: LayoutRecyclerviewImageBinding) :
        RecyclerView.ViewHolder(itemBinding.root){
        fun parcelBind(order: Any){
            itemBinding.addBaggageImage.setOnClickListener {
                listener.onAddClick(adapterPosition)
            }
        }

    }

    inner class DeleteViewHolder(private val itemBinding: LayoutBaggageImagesBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(order: Any) {
            Glide.with(itemView.baggage_image).load(order).centerCrop().into(itemBinding.baggageImage)
            itemBinding.deleteBaggage.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }
    }

    interface OnImageClickListener{
        fun onAddClick(position: Int)
        fun onDeleteClick(position: Int)
    }
}