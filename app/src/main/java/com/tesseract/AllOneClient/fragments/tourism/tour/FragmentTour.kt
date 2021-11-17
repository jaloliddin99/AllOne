package com.tesseract.AllOneClient.fragments.tourism.tour

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.FragmentImageAdapter
import com.tesseract.AllOneClient.adapter.tourism.TourImageAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismTourBinding
import com.tesseract.AllOneClient.model.tourism.ImageModel

class FragmentTour : Fragment() {
    private var binding: FragmentTourismTourBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTourismTourBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCard()

    }

    private fun setCard() {
        binding?.viewPager?.clipToPadding = false
        binding?.viewPager?.adapter =
            TourImageAdapter(this, getImage())
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
            ImageModel(R.drawable.mountain_stones),
            ImageModel(R.drawable.mountain_stones),
            ImageModel(R.drawable.mountain_stones),
        )
    }
}