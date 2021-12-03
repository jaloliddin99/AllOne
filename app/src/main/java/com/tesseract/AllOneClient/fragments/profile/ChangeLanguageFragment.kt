package com.tesseract.AllOneClient.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentChangeLanguageBinding
import com.tesseract.AllOneClient.utils.statusBarColor

class ChangeLanguageFragment: Fragment() {
    private var _binding: FragmentChangeLanguageBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentChangeLanguageBinding.inflate(inflater, container, false)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}