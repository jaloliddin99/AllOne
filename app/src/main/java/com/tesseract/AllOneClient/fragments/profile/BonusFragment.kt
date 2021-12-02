package com.tesseract.AllOneClient.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentBonusBinding

class BonusFragment: Fragment(R.layout.fragment_bonus) {

    private var _binding: FragmentBonusBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentBonusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.howToGetBonus.setOnClickListener {
            val action=BonusFragmentDirections.actionBonusFragment2ToFragmentGetBonusText()
            findNavController().navigate(action)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}