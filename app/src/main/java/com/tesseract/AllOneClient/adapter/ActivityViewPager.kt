package com.tesseract.AllOneClient.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tesseract.AllOneClient.fragments.charity.Main.FragmentCharityMain
import com.tesseract.AllOneClient.fragments.main.home.HomeMain.HomeFragment
import com.tesseract.AllOneClient.fragments.order.orderHome.OrderFragment
import com.tesseract.AllOneClient.fragments.profile.ProfileFragment

class ActivityViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> OrderFragment()
            2 -> FragmentCharityMain()
            else -> ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}