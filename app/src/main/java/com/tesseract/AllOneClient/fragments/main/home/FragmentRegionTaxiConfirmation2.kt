package com.tesseract.AllOneClient.fragments.main.home

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentRegionTaxiConfirmation2Binding

class FragmentRegionTaxiConfirmation2: Fragment(R.layout.fragment_region_taxi_confirmation2) {
    private var _binding: FragmentRegionTaxiConfirmation2Binding? =null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.bookNow.setOnClickListener {

            val action =FragmentRegionTaxiConfirmation2Directions.actionGlobalInterareaActiveOrder(7, "standard")
            findNavController().navigate(action)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}