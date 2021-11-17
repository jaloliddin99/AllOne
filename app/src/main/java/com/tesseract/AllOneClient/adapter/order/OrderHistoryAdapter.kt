package com.tesseract.AllOneClient.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.databinding.LayoutOrderListItemBinding
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryDataListModel

class OrderHistoryAdapter(
    private val newsList: ArrayList<OrderHistoryDataListModel>,
    private val context: Context
)
    : RecyclerView.Adapter<OrderHistoryAdapter.TaxiItemViewHolder>(){

    private lateinit var orderHistoryAdapterList: OrderHistoryAdapterList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaxiItemViewHolder {
        val binding=
            LayoutOrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaxiItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaxiItemViewHolder, position: Int) {

        val newsItem : OrderHistoryDataListModel =newsList[position]
        holder.bind(newsItem)

    }

    fun addList(list: List<OrderHistoryDataListModel>) {
        var counter=0
        counter+=itemCount
        newsList.addAll(list)
        notifyItemRangeInserted(counter, newsList.size)
    }

    fun clearList(){
        notifyItemRangeRemoved(0, newsList.size)
        newsList.clear()
    }
    override fun getItemCount()=newsList.size

    inner class TaxiItemViewHolder(private val itemBinding: LayoutOrderListItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(newsItemBinding: OrderHistoryDataListModel) {
            itemBinding.dateOfIssue.text=newsItemBinding.date
            orderHistoryAdapterList= OrderHistoryAdapterList(newsItemBinding.orders, context)
            itemBinding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemBinding.recyclerView.adapter=orderHistoryAdapterList
            itemBinding.recyclerView.setHasFixedSize(true)
        }
    }
}