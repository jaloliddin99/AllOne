package com.tesseract.AllOneClient.fragments.taxiCity.situation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentSituationBinding

class FragmentSituation: Fragment(R.layout.fragment_situation) {

    private var binding: FragmentSituationBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val situationBinding=FragmentSituationBinding.bind(view)
        binding=situationBinding

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

    }



}