package com.tesseract.AllOneClient.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.databinding.FragmentGetBonusBinding

class FragmentGetBonusText : Fragment() {
    var binding: FragmentGetBonusBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentGetBonusBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.back?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}