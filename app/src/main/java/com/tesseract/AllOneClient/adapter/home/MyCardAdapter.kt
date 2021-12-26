package com.tesseract.AllOneClient.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager.widget.PagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.fragments.main.home.payments.FragmentPayment
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData


class MyCardAdapter(
    private var cardsFragment: FragmentPayment,
    var list: List<GetCardData>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return list.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val cardViewNumber:TextView
        val itemCardName:TextView
        val linearLayout:LinearLayoutCompat
        val logoBrand:AppCompatImageView
        val v = LayoutInflater.from(cardsFragment.context).inflate(R.layout.layout_cards_to_donate, container, false)
        val cardNumFormat=(list[position].cardNumber!!).replaceRange(6, 12, "******")
        cardViewNumber=v.findViewById(R.id.item_card_number)
        itemCardName=v.findViewById(R.id.item_card_name)
        linearLayout=v.findViewById(R.id.mainCard)
        logoBrand=v.findViewById(R.id.logoBrand)
        if (list[position].type=="uzcard"){
            logoBrand.setImageResource(R.drawable.uzcard)
        }else if (list[position].type=="humo"){
            logoBrand.setImageResource(R.drawable.humo)
        }
        linearLayout.setBackgroundColor(android.graphics.Color.parseColor(list[position].cardColor))
        cardViewNumber.text= SaveData.formatCard(cardNumFormat)
        itemCardName.text=list[position].cardName



        container.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}