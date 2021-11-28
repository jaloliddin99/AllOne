package com.tesseract.AllOneClient.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.ItemInteriorChatMeBinding
import com.tesseract.AllOneClient.databinding.ItemInteriorChatYouBinding
import com.tesseract.AllOneClient.model.chat.Message
import android.graphics.BitmapFactory
import android.util.Base64
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_baggage_images.view.*


class ChatAdapter(var list: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val CHATME = 0
    private val YOUME = 1

    override fun getItemViewType(position: Int): Int {

        return when {
            (list[position].direction == "cd") -> {
                CHATME
            }
            else -> YOUME
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == CHATME) {
            return MyViewHolderMe(
                ItemInteriorChatMeBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
        return MyViewHolderYou(
            ItemInteriorChatYouBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolderYou) {
            holder.holderYou(list[position])
        }
        if(holder is MyViewHolderMe){
            holder.holderMe(list[position])
        }
    }

    override fun getItemCount()=list.size

    fun addMessage(message: Message){
        list.add(message)
        notifyItemInserted(list.size)
    }

    class MyViewHolderMe(var binding: ItemInteriorChatMeBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun holderMe(order:Message){
                if (order.type=="file"){
                    binding.txtTextMe.visibility= View.GONE
                    binding.card.visibility=View.VISIBLE
                    Picasso.get().load(order.content).into(binding.imageSend)
                }else{
                    binding.card.visibility=View.GONE
                    binding.txtTextMe.visibility=View.VISIBLE
                    binding.txtTextMe.text=order.content
                    binding.txtTimeMe.text=order.created_at
                }

            }
    }

    class MyViewHolderYou(var binding: ItemInteriorChatYouBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun holderYou(order:Message){
            if (order.type=="file"){
                binding.txtTextYou.visibility= View.GONE
                binding.card.visibility=View.VISIBLE
                Picasso.get().load(order.content).into(binding.imageSend)
            }else{
                binding.card.visibility=View.GONE
                binding.txtTextYou.visibility=View.VISIBLE
                binding.txtTextYou.text=order.content
                binding.txtTimeYou.text=order.created_at
            }
        }
    }
}