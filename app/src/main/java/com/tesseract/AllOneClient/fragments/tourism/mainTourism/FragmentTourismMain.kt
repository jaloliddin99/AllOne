package com.tesseract.AllOneClient.fragments.tourism.mainTourism

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.ClinicMainAdapter
import com.tesseract.AllOneClient.adapter.tourism.index.ExploreAdapter
import com.tesseract.AllOneClient.adapter.tourism.index.FragmentImageAdapter
import com.tesseract.AllOneClient.adapter.tourism.index.PopularPlacesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismMainBinding
import com.tesseract.AllOneClient.fragments.tourism.filter.TourismFilterViewModel
import com.tesseract.AllOneClient.fragments.tourism.tourPackages.TourPackagesViewModel
import com.tesseract.AllOneClient.model.medTourism.MainMedModel
import com.tesseract.AllOneClient.model.tourism.countries.TourismCountries
import com.tesseract.AllOneClient.model.tourism.main.index.Banner
import com.tesseract.AllOneClient.model.tourism.main.index.ExploreCountry
import com.tesseract.AllOneClient.model.tourism.main.index.PopularPlace
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentTourismMain : Fragment(),
    PopularPlacesAdapter.OnChipClickListener, ExploreAdapter.OnExploreListener {
    private var _binding: FragmentTourismMainBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: TourismMainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentTourismMainBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(TourismMainViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tourismMainIndex(headerMapUniversal(requireContext()))
        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        viewModel.tourismMainIndex.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE

            uzb_default_currency_id=it.content.uzb_default_currency_id
            world_default_currency_id=it.content.world_default_currency_id
            default_sort=it.content.default_sort
            uzbId=it.content.uzb_country_id
            setCard(it.content.banners)
            binding.apply {
                recyclerViewDiscover.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter=ExploreAdapter(this@FragmentTourismMain, it.content.explore_countries)
                }

            }


        })
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {

            view1.setOnClickListener {
                val action= FragmentTourismMainDirections.actionFragmentTourismMainToFragmentTourismPackages("uzbekistan",uzbId, uzb_default_currency_id, world_default_currency_id, default_sort)
                findNavController().navigate(action)
            }
            view2.setOnClickListener {
                val action= FragmentTourismMainDirections.actionFragmentTourismMainToFragmentTourismPackages("world", uzbId, uzb_default_currency_id, world_default_currency_id, default_sort)
                findNavController().navigate(action)
            }
            view3.setOnClickListener {
                val action=FragmentTourismMainDirections.actionGlobalTravelAgency(uzbId, default_sort)
                findNavController().navigate(action)
            }
            view5.setOnClickListener {
                val action=FragmentTourismMainDirections.actionGlobalCarRentMain()
                findNavController().navigate(action)
            }
            view6.setOnClickListener {
                val action=FragmentTourismMainDirections.actionGlobalMedOrTourFavourites("tourism")
                findNavController().navigate(action)
            }
            view4.setOnClickListener {
                val action=FragmentTourismMainDirections.actionGlobalHotelIndex(uzbId, uzb_default_currency_id, default_sort)
                findNavController().navigate(action)

            }
        }


        binding.explore.setOnClickListener {
            val action=FragmentTourismMainDirections.actionFragmentTourismMainToFragmentTourExplore()
            findNavController().navigate(action)
        }


    }

    private fun setCard(banner: List<Banner>) {
        binding.viewPager.clipToPadding = false
        binding.viewPager.adapter =
            FragmentImageAdapter(this, banner)
        binding.viewPager.pageMargin = 48

        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object :
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








    companion object{
        private var uzbId by Delegates.notNull<Int>()
        var uzb_default_currency_id by Delegates.notNull<Int>()
        var world_default_currency_id by Delegates.notNull<Int>()
        lateinit var default_sort :String
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onChipClicked(position: PopularPlace) {

    }

    override fun onExploreListener(position: ExploreCountry) {

        val action=FragmentTourismMainDirections.actionGlobalTourismExplore(position.id)
        findNavController().navigate(action)
    }
}