package com.tesseract.AllOneClient.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.CardTariffLayoutBinding
import com.tesseract.AllOneClient.model.home.routeRariffs.RouteTariffContentListModel
import kotlinx.android.synthetic.main.dialog_main_standart_share.*
import kotlinx.android.synthetic.main.dialog_main_standart_share.view.*


class TariffAdapter(
    private val context: Context,
    private val tariffList: List<RouteTariffContentListModel>,
    private val dialogCloseListener: DialogCloseListener
) : RecyclerView.Adapter<TariffAdapter.TariffItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TariffItemViewHolder {
        val binding= CardTariffLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TariffItemViewHolder(binding)
    }

    private var selectedPosition = -1

    override fun onBindViewHolder(holder: TariffItemViewHolder, position: Int) {
        val tariffItem : RouteTariffContentListModel =tariffList[position]
        holder.bind(tariffItem)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
        }

    }

    override fun getItemCount()=tariffList.size

    inner class TariffItemViewHolder(private val itemBinding: CardTariffLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root){


        @SuppressLint("SetTextI18n")
        fun bind(newsItemBinding: RouteTariffContentListModel){

            Picasso.get().load(newsItemBinding.icon).into(itemBinding.tariffCarImage)
            itemBinding.tariffType.text=newsItemBinding.name
            itemBinding.tariffPrice.text=SaveData.formatPhone(newsItemBinding.price!!)+" "+context.getString(R.string.summa1)

            if (newsItemBinding.discount?.toInt()!! >0){
                itemBinding.tariffSkidka.text=context.getString(R.string.discount)+" "+newsItemBinding.discount+"%"
                itemBinding.tariffSkidka.visibility=View.VISIBLE
            }else{
                itemBinding.tariffSkidka.visibility=View.GONE
            }
            if (adapterPosition==0){
                val color = ContextCompat.getColor(itemView.context, R.color.grey)
                itemBinding.cardTariff.setCardBackgroundColor(color)
            }else if (adapterPosition==1){
                val color = ContextCompat.getColor(itemView.context, R.color.purple_200)
                itemBinding.cardTariff.setCardBackgroundColor(color)
            }else{
                val color = ContextCompat.getColor(itemView.context, R.color.purple_100)
                itemBinding.cardTariff.setCardBackgroundColor(color)
            }


            itemBinding.changeTariff.setOnClickListener {
                showDialog(tariffList[adapterPosition].icon,
                    tariffList[adapterPosition].type,
                    tariffList[adapterPosition].name,
                    tariffList[adapterPosition].desc,
                    tariffList[adapterPosition].price
                )
                if (selectedPosition == adapterPosition) {
                    itemView.isSelected = true
                    itemBinding.cardTariff.strokeColor=itemView.context.getColor(R.color.green)
                    itemBinding.cardTariff.strokeWidth=6
                } else {
                    itemView.isSelected = false
                    itemBinding.cardTariff.strokeColor=itemView.context.getColor(R.color.white)
                }
                return@setOnClickListener
            }

            if (selectedPosition == adapterPosition) {
                itemView.isSelected = true
                dialogCloseListener.onDialogClose(
                    tariffList[adapterPosition].type,
                    tariffList[adapterPosition].name,
                    tariffList[adapterPosition].icon,
                    tariffList[adapterPosition].price
                )

                itemBinding.cardTariff.strokeColor=itemView.context.getColor(R.color.green)
                itemBinding.cardTariff.strokeWidth=6

            } else {
                itemView.isSelected = false
                itemBinding.cardTariff.strokeColor=itemView.context.getColor(R.color.white)
            }

        }
    }

    private fun showDialog(carImage: String?,type: String?, name: String?, description: String?, price:String? ){
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_main_standart_share, null)
        val mBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            .setView(mDialogView)

        val  mAlertDialog = mBuilder.show()

        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        mAlertDialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        Glide.with(context).load(carImage).into(mAlertDialog.car_image)
        mAlertDialog.window?.attributes?.windowAnimations = R.style.DialogAnimation;

        mAlertDialog.title.text=name
        mAlertDialog.description.text=description

        mDialogView.continue_button.setOnClickListener {
            mAlertDialog.dismiss()
            dialogCloseListener.onDialogClose(type, name, carImage, price)
        }

    }


    interface DialogCloseListener{
        fun onDialogClose(tariff: String?, name: String?,  url: String?, price: String?)
    }

}