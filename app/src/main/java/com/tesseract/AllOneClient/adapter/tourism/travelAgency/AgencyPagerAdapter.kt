package com.tesseract.AllOneClient.adapter.tourism.travelAgency

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentAboutClinic
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentClinicDoctors
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems.FragmentMedTurServices
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items.FragmentAgencyAboutFirm
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items.FragmentAgencyGallery
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items.FragmentAgencyTourPacket

class AgencyPagerAdapter(fm: FragmentManager, private val numOfTabs:Int, private val context: Context):
    FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return numOfTabs

    }

    private val tabTitles = arrayOf(
        context.getString(R.string.aboutFirm),
        context.getString(R.string.turPacket),
        context.getString(R.string.fotoGallery)
    )

    override fun getPageTitle(position: Int): CharSequence{
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment {
        var fragment= Fragment()
        when (position) {
            0 -> {
                fragment= FragmentAgencyAboutFirm()
            }
            1 -> {
                fragment=  FragmentAgencyTourPacket()
            }
            2 -> {
                fragment=  FragmentAgencyGallery()
            }

        }
        return fragment
    }


}