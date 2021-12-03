package com.tesseract.AllOneClient.fragments.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentTechnicalSupportBinding
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import com.tesseract.AllOneClient.utils.statusBarColor
import java.lang.Exception

class FragmentTechnicalSupport:Fragment() {

    private var _binding: FragmentTechnicalSupportBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentTechnicalSupportBinding.inflate(inflater, container, false)
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

        binding.telegram.setOnClickListener {
            gotoTelegram("@allone_call_center", requireContext())
        }

        binding.call.setOnClickListener {
            gotoContact(binding.phoneNumber.text.toString().replace(" ", ""), requireContext())
        }

        binding.faceBook.setOnClickListener {
            getFacebook()
        }
        binding.instagram.setOnClickListener {
            getInstagram()
        }

        binding.telegram1.setOnClickListener {
            gotoTelegram("@allone_call_center", requireContext())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    private fun getInstagram() {
        try {
            val instagramIntent = Intent(Intent.ACTION_VIEW)
            instagramIntent.data = Uri.parse("https://www.instagram.com/alloneuz/")
            startActivity(instagramIntent)
        } catch (e: Exception) {
            // show error message
        }
    }
    private fun getFacebook() {
        try {
            val instagramIntent = Intent(Intent.ACTION_VIEW)
            instagramIntent.data = Uri.parse("https://www.facebook.com/profile.php?id=100069513309716")
            startActivity(instagramIntent)
        } catch (e: Exception) {
            // show error message
        }
    }
}