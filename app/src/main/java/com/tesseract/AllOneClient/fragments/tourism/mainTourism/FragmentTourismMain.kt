package com.tesseract.AllOneClient.fragments.tourism.mainTourism

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.ClinicMainAdapter
import com.tesseract.AllOneClient.adapter.tourism.FragmentImageAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismMainBinding
import com.tesseract.AllOneClient.model.medTourism.MainMedModel
import com.tesseract.AllOneClient.model.tourism.ImageModel

class FragmentTourismMain : Fragment(R.layout.fragment_tourism_main), ClinicMainAdapter.OnImageClickListener {
    private var binding: FragmentTourismMainBinding?=null

    private lateinit var mainMedModel: List<MainMedModel>
    private lateinit var clinicMainAdapter: ClinicMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTourismMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.all?.setOnClickListener {
            val action=FragmentTourismMainDirections.actionFragmentTourismMainToFragmentHiking()
            findNavController().navigate(action)
        }

        loadItems()

        clinicMainAdapter=
            ClinicMainAdapter(mainMedModel, this)
        binding?.recyclerView?.adapter=clinicMainAdapter
        binding?.recyclerView?.layoutManager= GridLayoutManager(requireContext(), 2)
        binding?.recyclerView?.setHasFixedSize(true)

        setCard()

    }

    private fun setCard() {
        binding?.viewPager?.clipToPadding = false
        binding?.viewPager?.adapter =
            FragmentImageAdapter(this, getImage())
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




    private fun loadItems(){
        mainMedModel= listOf(
            MainMedModel("Тур пакеты", R.drawable.ic_travel_fly),
            MainMedModel("Гиды с авто", R.drawable.ic_travel_goude),
            MainMedModel("Rent - Car", R.drawable.ic_travel_big_car),
            MainMedModel("Походы", R.drawable.ic_children_bag)
        )
    }

    override fun onItemClick(position: Int) {
        val action= FragmentTourismMainDirections.actionFragmentTourismMainToFragmentTourPackets()
        findNavController().navigate(action)
    }
}