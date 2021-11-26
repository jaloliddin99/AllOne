package com.tesseract.AllOneClient.fragments.tourism.activeTour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.ActiveTourViewPager
import com.tesseract.AllOneClient.databinding.FragmentActiveTourBinding
import com.tesseract.AllOneClient.model.tourism.ImageModel

class FragmentActiveTour: Fragment() {
    private var binding: FragmentActiveTourBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveTourBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCard()
        setCard2()

    }

    private fun setCard() {
        binding?.viewPager?.clipToPadding = false
        binding?.viewPager?.adapter =
            ActiveTourViewPager(this, getImage())
        binding?.viewPager?.pageMargin = 48

        binding?.indicator?.setViewPager(binding?.viewPager)

        binding?.viewPager?.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun getImage(): ArrayList<ImageModel> {
        return arrayListOf(

        )
    }

    private fun setCard2() {
        binding?.viewPager2?.clipToPadding = false
        binding?.viewPager2?.adapter =
            ActiveTourViewPager(this, getImage())
        binding?.viewPager2?.pageMargin = 48

        binding?.indicator?.setViewPager(binding?.viewPager2)

        binding?.viewPager2?.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }


}