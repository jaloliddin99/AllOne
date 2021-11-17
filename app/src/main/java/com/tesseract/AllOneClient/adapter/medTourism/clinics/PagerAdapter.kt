package com.tesseract.AllOneClient.adapter.medTourism.clinics

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentAboutClinic
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentClinicDoctors
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentMedTurServices

class PagerAdapter(fm: FragmentManager, private val numOfTabs:Int, private val context: Context):FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return numOfTabs

    }

    private val tabTitles = arrayOf(
        context.getString(R.string.aboutClinic),
        context.getString(R.string.services),
        context.getString(R.string.doctors)
    )

    override fun getPageTitle(position: Int): CharSequence{
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        var fragment=Fragment()
         when (position) {
            0 -> {
                fragment= FragmentAboutClinic()
            }
            1 -> {
                fragment=  FragmentMedTurServices()
            }
            2 -> {
                fragment=  FragmentClinicDoctors()
            }

        }
        return fragment
    }


}