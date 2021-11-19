package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicDoctorsAdapter
import com.tesseract.AllOneClient.databinding.FragmentClinicDoctorsBinding
import com.tesseract.AllOneClient.dialogs.medTur.DialogDoctorView
import com.tesseract.AllOneClient.dialogs.medTur.DialogServices
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Doctor
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Service

class FragmentClinicDoctors:Fragment(), ClinicDoctorsAdapter.OnClickListener {
    private var _binding:FragmentClinicDoctorsBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: ClinicsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentClinicDoctorsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                recyclerView.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter=ClinicDoctorsAdapter(it.content.doctors as ArrayList<Doctor>, this@FragmentClinicDoctors)
                }
            })
        }
    }


    override fun onChipClicked(position: Int) {
        DialogDoctorView(position).show(parentFragmentManager, tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}