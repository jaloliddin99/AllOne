package com.tesseract.AllOneClient.adapter.tourism.hotel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentAboutClinic
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentClinicDoctors
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentMedTurServices
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.items.FragmentHotelContacts
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.items.FragmentHotelRooms

class HotelPagerAdapter(fm: FragmentManager, private val numOfTabs:Int, private val context: Context):
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return numOfTabs

    }

    private val tabTitles = arrayOf(
        context.getString(R.string.nomera),
        context.getString(R.string.contacts)
    )

    override fun getPageTitle(position: Int): CharSequence{
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        var fragment= Fragment()
        when (position) {
            0 -> {
                fragment= FragmentHotelRooms()
            }
            1 -> {
                fragment=  FragmentHotelContacts()
            }

        }
        return fragment
    }


}