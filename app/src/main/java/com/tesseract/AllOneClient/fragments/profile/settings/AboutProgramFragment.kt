package com.tesseract.AllOneClient.fragments.profile.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentAboutProgramBinding

class AboutProgramFragment:Fragment(R.layout.fragment_about_program) {

    private var binding: FragmentAboutProgramBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAboutProgramBinding=FragmentAboutProgramBinding.bind(view)

        binding=fragmentAboutProgramBinding

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}