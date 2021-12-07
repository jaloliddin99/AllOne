package com.tesseract.AllOneClient.adapter.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.charity.GoodHistoryAdapter
import com.tesseract.AllOneClient.adapter.charity.GoodHistoryItemsAdapter
import com.tesseract.AllOneClient.databinding.LayoutGoodDonationBinding
import com.tesseract.AllOneClient.model.charity.history.Charities
import com.tesseract.AllOneClient.model.profile.bonus.Bonuse
import com.tesseract.AllOneClient.model.profile.bonus.Data
import java.lang.Exception

class BonusItemAdapter(
    private val dataList:MutableSet<Data>,
    private val context: Context
)
    : RecyclerView.Adapter<BonusItemAdapter.ClinicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        val binding=
            LayoutGoodDonationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClinicViewHolder(binding)
    }
    fun addList(list: MutableSet<Data>) {
        var counter=0
        counter+=itemCount
        dataList.addAll(list)
        notifyItemRangeInserted(counter, dataList.size)
    }


    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val data : Data =dataList.elementAt(position)
        holder.bind(data)
    }

    override fun getItemCount()=dataList.size

    inner class ClinicViewHolder(private val itemBinding: LayoutGoodDonationBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(data: Data) {
            itemBinding.date.text=data.date
            itemBinding.recyclerView.layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            try {
                itemBinding.recyclerView.adapter=
                    BonusSubItemAdapter(data.bonuses as ArrayList<Bonuse>)
            }catch (e: Exception){

            }

        }


    }

}