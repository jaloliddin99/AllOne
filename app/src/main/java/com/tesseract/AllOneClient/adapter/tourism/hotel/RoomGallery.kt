package com.tesseract.AllOneClient.adapter.tourism.hotel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.dialogs.tourism.DialogHotelRoomView
import com.tesseract.AllOneClient.fragments.tourism.packagesView.FragmentPackagesView

class RoomGallery(
    private var homeFragment: DialogHotelRoomView,
    var list: List<String>
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
        Picasso.get().load(list[position]).into(imageView)

        container.addView(v)
        return v

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
}

