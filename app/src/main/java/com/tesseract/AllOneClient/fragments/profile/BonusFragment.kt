package com.tesseract.AllOneClient.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentBonusBinding

class BonusFragment: Fragment(R.layout.fragment_bonus) {

    private var fragmentBonusBinding: FragmentBonusBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentBonusBinding.bind(view)

        fragmentBonusBinding=binding
        binding.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.howToGetBonus.setOnClickListener {
            val action=BonusFragmentDirections.actionBonusFragment2ToFragmentGetBonusText()
            findNavController().navigate(action)
        }

    }


}