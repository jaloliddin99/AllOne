package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedTurAboutClinicBinding
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.utils.gotoContact

class FragmentAboutClinic:Fragment(), MedPhoneAdapter.OnClickListener {
    private var _binding:FragmentMedTurAboutClinicBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: ClinicsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMedTurAboutClinicBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                recyclerView.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter=ClinicImagesAdapter(it.content.gallery)
                    addr.text=it.content.addr


                    recyclerViewPhones.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    recyclerViewPhones.adapter= MedPhoneAdapter(it.content.phone_number, this@FragmentAboutClinic)



                    telegram.text=it.content.telegram
                    website.text=it.content.website
                    workTime.text=it.content.work_time
                    description.text=it.content.description

                }
            })

        }


    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}