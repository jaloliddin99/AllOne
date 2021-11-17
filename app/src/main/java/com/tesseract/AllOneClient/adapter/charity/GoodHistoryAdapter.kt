package com.tesseract.AllOneClient.adapter.charity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutGoodDonationBinding
import com.tesseract.AllOneClient.model.charity.history.Data
import com.tesseract.AllOneClient.model.charity.history.Order
import java.lang.Exception

class GoodHistoryAdapter(
    private val dataList:ArrayList<Data>,
    private val context: Context
)
    : RecyclerView.Adapter<GoodHistoryAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutGoodDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }
    fun addList(list: List<Data>) {
        var counter=0
        counter+=itemCount
        dataList.addAll(list)
        notifyItemRangeInserted(counter, dataList.size)
    }
    fun clearList(){
        dataList.clear()
        notifyItemRangeRemoved(0, 0)
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val data : Data =dataList[position]
        holder.bind(data)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutGoodDonationBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(data: Data) {
            itemBinding.date.text=data.date_by_words
            itemBinding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            try {
                itemBinding.recyclerView.adapter=GoodHistoryItemsAdapter(data.orders as ArrayList<Order>)
            }catch (e:Exception){

            }

        }


    }

}