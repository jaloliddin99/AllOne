package com.tesseract.AllOneClient.fragments.tourism.carRent.carRentMain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.tourism.carRent.CarRentIndexMainAdapter
import com.tesseract.AllOneClient.adapter.tourism.carRent.CarRentPopularAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourCarRentMainBinding
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.CarType
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.PopularBrand
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTourCarRentMain:Fragment(), CarRentPopularAdapter.OnChipClickListener, CarRentIndexMainAdapter.OnChipClickListener {
    private var _binding:FragmentTourCarRentMainBinding?=null
    private val binding get() = _binding!!

    private lateinit var viewModel: IndexViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTourCarRentMainBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(IndexViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.indexMainRequest(headerMapUniversal(requireContext()))

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.apply {
            viewModel.indexMain.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility=View.GONE
                recyclerView1.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                recyclerView1.adapter=CarRentPopularAdapter(this@FragmentTourCarRentMain, it.content.popular_brands)

                recyclerView2.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerView2.adapter=CarRentIndexMainAdapter(this@FragmentTourCarRentMain, it.content.car_types, requireContext())
            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding=null
    }

    override fun onChipClicked(position: PopularBrand) {

    }


    override fun onItemClicked(position: CarType) {
    }

}