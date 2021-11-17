package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.tabItems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicsServicesAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedTurServocesBinding
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Service

class FragmentMedTurServices: Fragment(), ClinicsServicesAdapter.CategoriesClickListener {
    private lateinit var binding:FragmentMedTurServocesBinding
    private val shareViewModel: ClinicsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMedTurServocesBinding.inflate(inflater, container, false)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                recyclerView.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter=ClinicsServicesAdapter(it.content.services, this@FragmentMedTurServices)
                }
            })
        }




    }

    override fun onChipClicked(position: Service) {

    }
}