package com.tesseract.AllOneClient.fragments.medTurism.ambulance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.medTourism.amb.AmbulanceAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedAmbulanceBinding
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.medTourism.ambulance.Content
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMedAmbulance:Fragment() , AmbulanceAdapter.CategoriesClickListener{
    private  var _binding:FragmentMedAmbulanceBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: AmbulanceViewModel

    private lateinit var adapter:AmbulanceAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMedAmbulanceBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(AmbulanceViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.ambulanceObserver(headerMapUniversal(requireContext()))

        binding.apply {
            viewModel.ambulance.observe(viewLifecycleOwner, {
                recyclerView.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    this@FragmentMedAmbulance.adapter=AmbulanceAdapter(it.content, this@FragmentMedAmbulance, requireContext())
                    adapter=this@FragmentMedAmbulance.adapter

                    loader.loader.visibility=View.GONE
                }
            })
        }

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })


        getBackStackData<DistrictList>("districtSelected", true) {
            binding.loader.loader.visibility = View.VISIBLE
        }
    }

    override fun onChipClicked(position: Content) {
        gotoContact(position.phone_number, requireContext())
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