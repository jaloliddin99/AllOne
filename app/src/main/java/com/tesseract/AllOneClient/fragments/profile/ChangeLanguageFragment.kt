package com.tesseract.AllOneClient.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentChangeLanguageBinding

class ChangeLanguageFragment: Fragment(R.layout.fragment_change_language) {
    private var fragmentChangeLanguageFragment: FragmentChangeLanguageBinding?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding=FragmentChangeLanguageBinding.bind(view)
        fragmentChangeLanguageFragment=binding

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        if (SaveData.getUzbek(requireContext())){
            binding.uzbek.isChecked=true
        }
        if (SaveData.getRussian(requireContext())){
            binding.russian.isChecked=true
        }
        if (SaveData.getEnglish(requireContext())){
            binding.english.isChecked=true
        }


        binding.russian.setOnClickListener {
            SaveData.setIsRussian(requireContext(), true)
            SaveData.setIsEnglish(requireContext(), false)
            SaveData.setIsUzbek(requireContext(), false)
            SaveData.setLanguage(requireContext(), "ru")
            restartActivity()
        }
        binding.english.setOnClickListener {
            SaveData.setIsEnglish(requireContext(), true)
            SaveData.setIsUzbek(requireContext(), false)
            SaveData.setIsRussian(requireContext(), false)
            SaveData.setLanguage(requireContext(), "en")
            restartActivity()
        }
        binding.uzbek.setOnClickListener {
            SaveData.setIsUzbek(requireContext(), true)
            SaveData.setIsRussian(requireContext(), false)
            SaveData.setIsEnglish(requireContext(), false)
            SaveData.setLanguage(requireContext(), "uz")
            restartActivity()
        }
    }

    private fun restartActivity(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}