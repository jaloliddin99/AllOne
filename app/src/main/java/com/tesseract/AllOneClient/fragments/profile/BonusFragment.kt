package com.tesseract.AllOneClient.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentBonusBinding
import com.tesseract.AllOneClient.utils.statusBarColor

class BonusFragment: Fragment(R.layout.fragment_bonus) {

    private var _binding: FragmentBonusBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentBonusBinding.inflate(inflater, container, false)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )
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