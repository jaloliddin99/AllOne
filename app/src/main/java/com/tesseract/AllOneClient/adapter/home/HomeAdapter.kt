package com.tesseract.AllOneClient.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.LayoutActiveOrderCityBinding
import com.tesseract.AllOneClient.databinding.LayoutActiveOrderParcelItemBinding
import com.tesseract.AllOneClient.databinding.LayoutActiveOrderRegionItemBinding
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderList
import com.tesseract.AllOneClient.model.order.MessageEventActiveOrder
import org.greenrobot.eventbus.EventBus


class HomeAdapter(
    private val activeDataList: List<OrderList>,
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ORDER_ITEM = 0
    private val PARCEL_ITEM = 1
    private val City_item = 2


    override fun getItemViewType(position: Int): Int {
        var value = -1
        value = when {
            activeDataList[position].orderType.equals("interarea_parcel_delivery") -> {
                PARCEL_ITEM
            }
            activeDataList[position].orderType.equals("interarea") -> {
                ORDER_ITEM
            }
            else -> City_item
        }
        return value
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType:
        Int
    ): RecyclerView.ViewHolder {

        var viewHolder: RecyclerView.ViewHolder? = null

        viewHolder = when (viewType) {
            PARCEL_ITEM -> {
                val binding =
                    LayoutActiveOrderParcelItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                ParcelViewHolder(binding)
            }
            ORDER_ITEM -> {
                val binding =
                    LayoutActiveOrderRegionItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                OrderViewHolder(binding)
            }
            else -> {
                val binding =
                    LayoutActiveOrderCityBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                CityViewHolder(binding)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        if (holder is ParcelViewHolder) {
            holder.parcelBind(activeDataList[position])
        }
        if (holder is OrderViewHolder) {
            holder.bind(activeDataList[position])
        }

        if (holder is CityViewHolder) {
            holder.city(activeDataList[position])
        }

    }

    override fun getItemCount() = activeDataList.size

    inner class ParcelViewHolder(private val itemBinding: LayoutActiveOrderParcelItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun parcelBind(order: OrderList) {
            itemBinding.parcel.podrobne.setOnClickListener {
                EventBus.getDefault().post(MessageEventActiveOrder(order.orderType, order.id))
            }
            itemBinding.parcel.title.text = order.title
            itemBinding.parcel.tariff.text = order.tariff+": "
            itemBinding.parcel.orderNumber.text = "№${order.id}"
            itemBinding.parcel.startDestination.text = order.from
            itemBinding.parcel.endDestination.text = order.to
            itemBinding.parcel.dealMoney.text =
                order.amount?.let { SaveData.formatPhone(it) } + " " + context.getString(com.tesseract.AllOneClient.R.string.summa1)
            itemBinding.parcel.status.text=order.status

        }

    }

    inner class OrderViewHolder(private val itemBinding: LayoutActiveOrderRegionItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(order: OrderList) {
            itemBinding.region.podrobne.setOnClickListener {
                EventBus.getDefault()
                    .post(MessageEventActiveOrder(order.orderType, order.id))
            }

            itemBinding.region.title.text = order.title
            itemBinding.region.tariff.text = order.tariff+": "
            itemBinding.region.orderNumber.text ="№${order.id}"
            itemBinding.region.bannedSeats.text = order.places
            itemBinding.region.status.text = order.status
            itemBinding.region.startDestination.text = order.from
            itemBinding.region.endDestination.text = order.to
            itemBinding.region.dealMoney.text =
                order.amount?.let { SaveData.formatPhone(it) } + " " + context.getString(com.tesseract.AllOneClient.R.string.summa1)

        }
    }


    inner class CityViewHolder(private val itemBinding: LayoutActiveOrderCityBinding) :
        RecyclerView.ViewHolder(itemBinding.root){

        @SuppressLint("SetTextI18n")
        fun city(order: OrderList) {
            itemBinding.city.podrobne.setOnClickListener {
                EventBus.getDefault()
                    .post(MessageEventActiveOrder(order.orderType, order.id))
            }

            itemBinding.city.title.text = order.title
            itemBinding.city.tariff.text = order.tariff+": "
            itemBinding.city.orderNumber.text = "№${order.id}"
            itemBinding.city.status.text = order.status
            itemBinding.city.dealMoney.text =
                order.amount?.let { SaveData.formatPhone(it) } + " " + context.getString(com.tesseract.AllOneClient.R.string.summa1)

        }

    }

}