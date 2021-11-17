package com.tesseract.AllOneClient.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentTechnicalSupportBinding

class FragmentTechnicalSupport:Fragment(R.layout.fragment_technical_support) {

    private var binding: FragmentTechnicalSupportBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentTechnicalSupport=FragmentTechnicalSupportBinding.bind(view)
        binding=fragmentTechnicalSupport
        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}