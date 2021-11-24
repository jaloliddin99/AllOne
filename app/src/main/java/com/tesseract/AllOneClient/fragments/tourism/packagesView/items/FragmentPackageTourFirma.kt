package com.tesseract.AllOneClient.fragments.tourism.packagesView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourAgencyBinding
import com.tesseract.AllOneClient.fragments.tourism.packagesView.PackageViewModel
import com.tesseract.AllOneClient.utils.gotoContact

class FragmentPackageTourFirma: Fragment(), MedPhoneAdapter.OnClickListener {
    private var _binding:FragmentTourAgencyBinding?=null
    private val shareViewModel: PackageViewModel by activityViewModels()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTourAgencyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                addr.text=it.content.agency_addr
                Picasso.get().load(it.content.agency_poster).into(poster)
                agencyName.text=it.content.agency_name


                recyclerViewPhones.layoutManager=
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter= MedPhoneAdapter(it.content.agency_phone_number, this@FragmentPackageTourFirma)


                if (it.content.agency_closed){

                }else{
                    agencyClosed.visibility=View.GONE
                }

                telegram.text=it.content.agency_telegram
                website.text=it.content.agency_website
                workTime.text=it.content.agency_work_time
                description.text=it.content.agency_description

            })
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }
}