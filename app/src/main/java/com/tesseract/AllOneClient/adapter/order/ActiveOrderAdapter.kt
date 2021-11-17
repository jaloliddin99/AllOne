package com.tesseract.AllOneClient.adapter.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.adapter.home.HomeAdapter
import com.tesseract.AllOneClient.databinding.LayoutOrderListItemBinding
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryDataListModel

class ActiveOrderAdapter(
    private val activeList: ArrayList<OrderHistoryDataListModel>,
    private val context: Context
)
    : RecyclerView.Adapter<ActiveOrderAdapter.ViewModel>(){

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding=
            LayoutOrderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewModel(binding)
    }

    fun addList(list: List<OrderHistoryDataListModel>) {
        var counter=0
        counter+=itemCount
        activeList.addAll(list)
        notifyItemRangeInserted(counter, activeList.size)
    }

    fun clearList(){
        notifyItemRangeRemoved(0, activeList.size)
        activeList.clear()
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {

        val newsItem : OrderHistoryDataListModel =activeList[position]
        holder.bind(newsItem)

    }
    override fun getItemCount()=activeList.size

    inner class ViewModel(private val itemBinding: LayoutOrderListItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(newsItemBinding: OrderHistoryDataListModel) {
            itemBinding.dateOfIssue.text=newsItemBinding.date
            homeAdapter= HomeAdapter(newsItemBinding.orders, context)
            itemBinding.recyclerView.layoutManager=
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemBinding.recyclerView.adapter=homeAdapter
            itemBinding.recyclerView.setHasFixedSize(true)
        }
    }
}