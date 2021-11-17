package com.tesseract.AllOneClient.adapter.tourism

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.fragments.tourism.mainTourism.FragmentTourismMain
import com.tesseract.AllOneClient.model.tourism.ImageModel

class FragmentImageAdapter(
    private var homeFragment: FragmentTourismMain,
    var list: List<ImageModel>
) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return list.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView: ImageView

        val v =
            LayoutInflater.from(homeFragment.context)
                .inflate(R.layout.fragment_image_card, container, false)

        imageView = v.findViewById(R.id.image)
        imageView.setImageResource(list[position].image)

        container.addView(v)
        return v

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}

