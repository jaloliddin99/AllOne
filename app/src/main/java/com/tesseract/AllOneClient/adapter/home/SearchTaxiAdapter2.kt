package com.tesseract.AllOneClient.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.LayoutSearchTaxisQueryBinding
import com.tesseract.AllOneClient.model.home.SearchModel.OtherOption
import com.tesseract.AllOneClient.model.home.SearchModel.YourRequest

class SearchTaxiAdapter2(
    private var context: Context,
    private var yourRequest: List<YourRequest>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SearchTaxiAdapter2.SearchTaxisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTaxisViewHolder {
        val binding=
            LayoutSearchTaxisQueryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchTaxisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchTaxisViewHolder, position: Int) {
        val searchItem : YourRequest =yourRequest[position]
        holder.bind(searchItem)
    }

    override fun getItemCount()=yourRequest.size


    inner class SearchTaxisViewHolder(private val itemBinding: LayoutSearchTaxisQueryBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{

        fun bind(yourRequest: YourRequest) {
            if (yourRequest.has_luggage){
                itemBinding.hasLuggage.text=context.getString(R.string.large_baggage)
            }else{
                itemBinding.hasLuggage.text=context.getString(R.string.withoutBaggage)
            }
            if (yourRequest.has_conditioner){
                itemBinding.hasConditioner.text=context.getString(R.string.has_air_conditioner)
            }else{
                itemBinding.hasConditioner.text=context.getString(R.string.withoutConditioner)
            }
            if (yourRequest.free_places[0].toString()=="0"){
                itemBinding.rec1.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                itemBinding.rec1.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (yourRequest.free_places[1].toString()=="0"){
                itemBinding.rec4.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                itemBinding.rec4.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (yourRequest.free_places[2].toString()=="0"){
                itemBinding.rec3.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                itemBinding.rec3.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (yourRequest.free_places[3].toString()=="0"){
                itemBinding.rec2.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                itemBinding.rec2.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            var counter=0
            for (element in yourRequest.free_places){
                if (element.toString()=="0"){
                    counter++
                }
            }
            itemBinding.freePlacesNumber.text=counter.toString()
            itemBinding.baggage.text=yourRequest.baggage
            itemBinding.driverCar.text=yourRequest.driver_car
            itemBinding.driverLocation.text=yourRequest.driver_location

        }

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position:Int=adapterPosition
            if (position!= RecyclerView.NO_POSITION){
                listener.onOtherOptionsClick(position)
            }
        }

    }

    interface OnItemClickListener{
        fun onOtherOptionsClick(position: Int)
    }
}