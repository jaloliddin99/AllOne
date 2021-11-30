package com.tesseract.AllOneClient.fragments.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_splash.*
import com.tesseract.AllOneClient.model.order.MessageEvent

import org.greenrobot.eventbus.EventBus




@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash){

    private lateinit var binding: FragmentSplashBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val splashBinding=FragmentSplashBinding.bind(view)



        binding=splashBinding
        binding.enLan.setOnClickListener{
            SaveData.setIsEnglish(requireContext(), true)
            SaveData.setIsUzbek(requireContext(), false)
            SaveData.setIsRussian(requireContext(), false)
            EventBus.getDefault().post("English")
            SaveData.setLanguage(requireContext(), "en")
            navigator()
//
       }

        binding.rusLan.setOnClickListener {
            SaveData.setIsRussian(requireContext(), true)
            SaveData.setIsEnglish(requireContext(), false)
            SaveData.setIsUzbek(requireContext(), false)
            EventBus.getDefault().post("Russian")
            SaveData.setLanguage(requireContext(), "ru")
            navigator()
        }
        binding.uzbLan.setOnClickListener {
            SaveData.setIsUzbek(requireContext(), true)
            SaveData.setIsRussian(requireContext(), false)
            SaveData.setIsEnglish(requireContext(), false)
            SaveData.setLanguage(requireContext(), "uz")
            EventBus.getDefault().post("Uzbek")
            navigator()
        }

    }



    private fun navigator(){
        val action=SplashFragmentDirections.actionSplashFragmentToSignInFragment()
        findNavController().navigate(action)
    }




}