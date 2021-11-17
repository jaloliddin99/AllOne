package com.tesseract.AllOneClient.fragments.main.home.SearchTaxi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.SearchTaxiAdapter2
import com.tesseract.AllOneClient.adapter.home.SearchTaxisAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentSearchTaxisBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.model.home.SearchModel.YourRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearchTaxi : Fragment(R.layout.fragment_search_taxis),
    SearchTaxisAdapter.OnItemClickListener, SearchTaxiAdapter2.OnItemClickListener {
    private var binding: FragmentSearchTaxisBinding? = null
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private lateinit var searchTaxisAdapter: SearchTaxisAdapter
    private lateinit var searchTaxiAdapter2: SearchTaxiAdapter2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchTaxisBinding = FragmentSearchTaxisBinding.bind(view)
        binding = searchTaxisBinding

        viewModelListener()
    }

    private fun viewModelListener(){
        shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
            loadItems(it)
            searchTaxisAdapter = SearchTaxisAdapter(requireContext(), it.your_request as ArrayList<YourRequest>,  this)
            binding?.recyclerQuery?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerQuery?.adapter = searchTaxisAdapter

            searchTaxiAdapter2 = SearchTaxiAdapter2(requireContext(), it.other_options,  this)
            binding?.recyclerViewOptions?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding?.recyclerViewOptions?.adapter = searchTaxiAdapter2
        })
    }
    @SuppressLint("SetTextI18n")
    private fun loadItems(it: Content){
        binding?.apply {
            topReuse.found.text=it.found
            tariff.text=it.order.tariff
            places.text=it.order.places
            price.text=SaveData.formatPhone(it.order.price)+context?.getString(R.string.summa1)
        }
    }

    override fun onItemClick(position: Int) {
        val action =
            FragmentSearchTaxiDirections.actionFragmentSearchTaxiToFragmentRegionDriverInfo( position, true)
        findNavController().navigate(action)
    }

    override fun onOtherOptionsClick(position: Int) {
        val action =
            FragmentSearchTaxiDirections.actionFragmentSearchTaxiToFragmentRegionDriverInfo(position, false)
        findNavController().navigate(action)
    }


}