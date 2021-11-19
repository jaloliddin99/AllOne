package com.tesseract.AllOneClient.fragments.medTurism.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicsAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedTurDoctorsBinding
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.medTourism.categories.ClinicsCategoriesModel
import com.tesseract.AllOneClient.model.medTourism.categories.Content
import com.tesseract.AllOneClient.model.medTourism.clinics.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMedTurDoctors:Fragment(), ClinicsAdapter.OnClickListener {
    private var _binding: FragmentMedTurDoctorsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:DoctorsViewModel

    private lateinit var adapter: ClinicsAdapter
    private var isCurrentFragment: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMedTurDoctorsBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(DoctorsViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.doctorPaging = 1
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        if (isCurrentFragment){
            viewModel.startDoctors(headerMapUniversal(requireContext()), "", content.id, district.id!!)
        }
        viewModel.getClinicCategories(headerMapUniversal(requireContext()))

        viewModel.errorMessageDoctor.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        adapter = ClinicsAdapter(mutableSetOf(), this)

        binding.apply {
            loader.loader.visibility = View.VISIBLE
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager2
            recyclerView.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.getDoctor(
                        headerMapUniversal(requireContext()),
                        "",
                        content.id,
                        district.id!!
                    )
                }
            })

            byCategories.setOnClickListener {
                if (isInitialized) {
                    findNavController().navigate(
                        FragmentMedTurDoctorsDirections.actionGlobalMedSearch(
                            categoryList,
                            true
                        )
                    )
                    isCurrentFragment = false
                } else {
                    loader.loader.visibility = View.VISIBLE
                    viewModel.getClinicCategories(headerMapUniversal(requireContext()))
                }
            }

            location.setOnClickListener {
                findNavController().navigate(
                    FragmentMedTurDoctorsDirections.actionGlobalMedSearch(
                        categoryList,
                        false
                    )
                )
                isCurrentFragment = false
            }
        }
        clinicsAdapterSetter()
    }

    private fun clinicsAdapterSetter() {

        viewModel.errorClinicCategories.observe(viewLifecycleOwner,{
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        val arrayList:MutableSet<Data> =HashSet()
        viewModel.getDoctorsObserver.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            for (i in it.indices) {
                arrayList.addAll(it)
            }
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
        })

        getBackStackData<Content>("categoryMed", true) {
            binding.byCategories.text = it.name
            content = it
            if (district.id != 1) {
                binding.location.text = district.name
                viewModel.startDoctors(
                    headerMapUniversal(requireContext()),
                    "",
                    content.id,
                    district.id!!
                )
            } else {
                viewModel.startDoctors(headerMapUniversal(requireContext()), "", content.id, 1)
            }
            Common.doctorPaging = 1
            binding.loader.loader.visibility = View.VISIBLE
        }

        getBackStackData<DistrictList>("districtSelected", true) {
            binding.location.text = it.name
            district = it
            Toast.makeText(context, content.name, Toast.LENGTH_SHORT).show()
            if (content.id != 1) {
                binding.byCategories.text = content.name
                viewModel.startDoctors(
                    headerMapUniversal(requireContext()),
                    "",
                    content.id,
                    it.id!!
                )
            } else {
                viewModel.startDoctors(headerMapUniversal(requireContext()), "", 1, it.id!!)
            }
            Common.doctorPaging = 1
            binding.loader.loader.visibility = View.VISIBLE

        }

        viewModel.categories.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            isInitialized = true
            categoryList = it
        })

    }

    private var district = DistrictList(1, "")

    private var content = Content(1, "")

    lateinit var categoryList: ClinicsCategoriesModel
    private var isInitialized: Boolean = false

    override fun onChipClicked(position: Int) {
        val action=FragmentMedTurDoctorsDirections.actionFragmentMedTurDoctorsToFragmentDoctorView(position)
        findNavController().navigate(action)
    }

    private fun <T> Fragment.getBackStackData(
        key: String,
        singleCall: Boolean = true,
        result: (T) -> (Unit)
    ) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                result(it)
                if (singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(
                    key
                )
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}