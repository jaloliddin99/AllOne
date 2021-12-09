package com.tesseract.AllOneClient.fragments.profile.techSupport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentTechnicalSupportBinding
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class FragmentTechnicalSupport:Fragment() {

    private var _binding: FragmentTechnicalSupportBinding?=null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactViewModel
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
        viewModel=ViewModelProvider(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.contacts(headerMapUniversal(requireContext()))
        viewModel.contactError.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })

        viewModel.contacts.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE

            binding.locationName.text=it.content.address

            binding.telegram.setOnClickListener {view->
                gotoTelegram(it.content.tg_account, requireContext())
            }

            binding.telegram1.setOnClickListener {view->
                gotoTelegram(it.content.tg_account, requireContext())
            }

            binding.call.setOnClickListener {view->
                gotoContact(it.content.phone_number, requireContext())
            }

        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }




        binding.faceBook.setOnClickListener {
            getFacebook()
        }
        binding.instagram.setOnClickListener {
            getInstagram()
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