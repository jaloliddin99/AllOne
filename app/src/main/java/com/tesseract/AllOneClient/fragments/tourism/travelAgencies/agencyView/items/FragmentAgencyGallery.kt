package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.tourism.travelAgency.GalleryAgencyAdapter
import com.tesseract.AllOneClient.databinding.FragmentAgencyGalleryBinding
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.AgencyViewModel

class FragmentAgencyGallery:Fragment(),GalleryAgencyAdapter.OnChipClickListener {
    private var _binding:FragmentAgencyGalleryBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: AgencyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentAgencyGalleryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {

                recyclerView.layoutManager=GridLayoutManager(context, 2)
                recyclerView.adapter= GalleryAgencyAdapter( this@FragmentAgencyGallery, it.content.gallery)

            })
        }

    }

    override fun onItemClicked(position: String) {

    }
}