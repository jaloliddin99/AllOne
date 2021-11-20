package com.tesseract.AllOneClient.adapter.tourism.tourPackages

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentAboutClinic
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentClinicDoctors
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentMedTurServices
import com.tesseract.AllOneClient.fragments.tourism.packagesView.items.FragmentPackageItemDescription
import com.tesseract.AllOneClient.fragments.tourism.packagesView.items.FragmentPackageTourFirma

class TourismPagerAdapter(fm: FragmentManager, private val numOfTabs:Int, private val context: Context):
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return numOfTabs

    }

    private val tabTitles = arrayOf(
        context.getString(R.string.desciption),
        context.getString(R.string.tourFirma)
    )

    override fun getPageTitle(position: Int): CharSequence{
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        var fragment= Fragment()
        when (position) {
            0 -> {
                fragment= FragmentPackageItemDescription()
            }
            1 -> {
                fragment=  FragmentPackageTourFirma()
            }


        }
        return fragment
    }


}