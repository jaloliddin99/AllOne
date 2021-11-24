package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentAgencyAboutFirmBinding
import com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView.AgencyViewModel
import com.tesseract.AllOneClient.utils.gotoContact

class FragmentAgencyAboutFirm: Fragment(), MedPhoneAdapter.OnClickListener {
    private var _binding:FragmentAgencyAboutFirmBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: AgencyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentAgencyAboutFirmBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {

                addr.text=it.content.addr
                recyclerViewPhones.layoutManager=
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter= MedPhoneAdapter(it.content.phone_number, this@FragmentAgencyAboutFirm)

                telegram.text=it.content.telegram
                website.text=it.content.website
                description.text=it.content.description


            })
        }


    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }
}