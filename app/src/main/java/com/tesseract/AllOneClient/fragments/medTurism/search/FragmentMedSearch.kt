package com.tesseract.AllOneClient.fragments.medTurism.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.RegionRegionAdapter
import com.tesseract.AllOneClient.adapter.home.SearchDistrictAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicCategoriesAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.DistrictAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.RegionAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedCategoryBinding
import com.tesseract.AllOneClient.fragments.main.home.district.DistrictViewModel
import com.tesseract.AllOneClient.fragments.main.home.regionList.RegionViewModel
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import com.tesseract.AllOneClient.model.medTourism.categories.Content
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class FragmentMedSearch : Fragment(),
    ClinicCategoriesAdapter.CategoriesClickListener,
    RegionAdapter.OnItemClickListener,
    DistrictAdapter.OnItemClick {
    private var _binding: FragmentMedCategoryBinding?=null
    private val binding get() = _binding!!
    private val args: FragmentMedSearchArgs by navArgs()

    private lateinit var viewModel: RegionViewModel
    private lateinit var regionAdapter: RegionAdapter
    private lateinit var districtAdapter: DistrictAdapter
    private lateinit var districtVIewModel: DistrictViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RegionViewModel::class.java)
        districtVIewModel = ViewModelProvider(this).get(DistrictViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            if (args.isCategories) {
                loader.loader.visibility = View.GONE
                recyclerView.apply {
                    adapter = ClinicCategoriesAdapter(args.content.content, this@FragmentMedSearch)
                }
            } else {
                title.text = getString(R.string.select_region)
                viewModel.getRegionList(headerMapUniversal(requireContext()))
                loader.loader.visibility = View.VISIBLE
                viewModel.regionDetails.observe(viewLifecycleOwner, {
                    loader.loader.visibility = View.GONE
                    regionAdapter = RegionAdapter(it, this@FragmentMedSearch)
                    binding.recyclerView.adapter = regionAdapter
                })

                districtVIewModel.districtList.observe(viewLifecycleOwner, {
                    districtAdapter = DistrictAdapter(it, this@FragmentMedSearch)
                    headerText.text = this@FragmentMedSearch.headerText
                    binding.recyclerView.adapter = districtAdapter
                    loader.loader.visibility = View.GONE
                })
            }
            cancel.setOnClickListener {
                findNavController().popBackStack()
            }

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    try {
                        regionAdapter.filter.filter(newText)
                        districtAdapter.filter.filter(newText)
                    } catch (e: Exception) {

                    }
                    return true
                }

            })
        }
    }

    override fun onChipClicked(position: Content) {
        setBackStackData("categoryMed", position, true)
    }

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    private var headerText = ""
    override fun onItemClick(searchItemBinding: GetRegionDetails) {
        headerText = searchItemBinding.name.toString()
        districtVIewModel.getDistrictDetails(
            headerMapUniversal(requireContext()),
            searchItemBinding.id.toString()
        )
        binding.apply {
            loader.loader.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(district: DistrictList) {
        setBackStackData("districtSelected", district, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}