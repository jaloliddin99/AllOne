package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.tourism.travelAgency.AgencyPackagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentAgencyTourPacketsBinding
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.AgencyViewModel
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.FragmentAgencyView
import com.tesseract.AllOneClient.model.tourism.agency.packageView.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAgencyTourPacket:Fragment() ,AgencyPackagesAdapter.OnExploreListener{
    private var _binding:FragmentAgencyTourPacketsBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: AgencyPackagesAdapter

    private lateinit var viewModel: AgencyViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAgencyTourPacketsBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(AgencyViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.agencyPackagesPager=1

        viewModel.startAgencyPackageView(headerMapUniversal(requireContext()), FragmentAgencyView.agencyId)
        adapter = AgencyPackagesAdapter(this, mutableSetOf())

        binding.apply {
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerViewTourPacket.adapter = adapter
            recyclerViewTourPacket.layoutManager = layoutManager2
            recyclerViewTourPacket.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.agencyPackageView(headerMapUniversal(requireContext()), FragmentAgencyView.agencyId)
                }
            })

        }
        adapterSet()



    }


    private fun adapterSet() {
        val arrayList: MutableSet<Data> = HashSet()
        viewModel.agencyPackageView.observe(viewLifecycleOwner, {
            arrayList.addAll(it.content.data)
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }

    override fun onExploreListener(position: Data) {

    }
}