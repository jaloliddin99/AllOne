package com.tesseract.AllOneClient.adapter.charity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.LayoutCardsToDonateBinding
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData

class SelectCardToDonate(
    var cardList:ArrayList<GetCardData>,
    private val listener: OnItemClickListener,
    private val context: Context
)
    : RecyclerView.Adapter<SelectCardToDonate.AddCardItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddCardItemViewHolder {
        val binding= LayoutCardsToDonateBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddCardItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddCardItemViewHolder, position: Int) {
        val cardListt : GetCardData =cardList[position]
        holder.bind(cardListt)
    }
    override fun getItemCount()=cardList.size

    inner class AddCardItemViewHolder(private val itemBinding: LayoutCardsToDonateBinding)
        : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener{


        @SuppressLint("SetTextI18n")
        fun bind(card: GetCardData) {
            itemBinding.itemCardName.text=card.cardName

            val cardNumFormat=(card.cardNumber!!).replaceRange(6, 12, "******")
            itemBinding.itemCardNumber.text= SaveData.formatCard(cardNumFormat)
            itemBinding.mainCard.setBackgroundColor(Color.parseColor(card.cardColor))
            itemBinding.balance.text=SaveData.formatPhone(card.balance!!)+" "+context.getString(R.string.summa1)
            if (card.type=="uzcard"){
                itemBinding.logoBrand.setImageResource(R.drawable.uzcard)
            }else if (card.type=="humo"){
                itemBinding.logoBrand.setImageResource(R.drawable.humo)
            }
        }
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val card: GetCardData =cardList[adapterPosition]
            if (adapterPosition!= RecyclerView.NO_POSITION){
                listener.onItemClick(card)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(card: GetCardData)
    }
}