package com.tesseract.AllOneClient.adapter.order

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutOrderListListItemBinding
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderList
import com.tesseract.AllOneClient.model.order.MessageEvent
import org.greenrobot.eventbus.EventBus


class OrderHistoryAdapterList(
    private val newsList: List<OrderList>,
    private val context: Context
) : RecyclerView.Adapter<OrderHistoryAdapterList.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            LayoutOrderListListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val newsItem: OrderList = newsList[position]
        holder.bind(newsItem)

    }

    override fun getItemCount() = newsList.size

    inner class MyViewHolder(private val itemBinding: LayoutOrderListListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(newsItemBinding: OrderList) {
            when {
                newsItemBinding.orderType.equals("interarea_parcel_delivery") -> {
                    itemView.setOnClickListener {
                        EventBus.getDefault()
                            .post(newsItemBinding.id?.let { it1 ->
                                MessageEvent(adapterPosition, newsItemBinding.orderType,
                                    it1
                                )
                            })
                    }
                }
                newsItemBinding.orderType.equals("interarea") -> {
                    itemView.setOnClickListener {
                        EventBus.getDefault()
                            .post(newsItemBinding.id?.let { it1 ->
                                MessageEvent(adapterPosition, newsItemBinding.orderType,
                                    it1
                                )
                            })

                    }
                }
                else -> {
                    itemView.setOnClickListener {
                        EventBus.getDefault()
                            .post(newsItemBinding.id?.let { it1 ->
                                MessageEvent(adapterPosition, newsItemBinding.orderType,
                                    it1
                                )
                            })
                    }
                }
            }

            itemBinding.taxiCard.tariff.text = newsItemBinding.tariff + ": "
            itemBinding.taxiCard.orderType.text = newsItemBinding.title
            itemBinding.taxiCard.time.text = newsItemBinding.time
            itemBinding.taxiCard.dealMoney.text = newsItemBinding.amount?.split(".")
                ?.get(0) + " " + context.getString(R.string.summa1)

        }
    }

}