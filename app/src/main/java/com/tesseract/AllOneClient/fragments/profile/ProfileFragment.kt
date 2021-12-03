package com.tesseract.AllOneClient.fragments.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.getBalance
import com.tesseract.AllOneClient.constants.SaveData.getName
import com.tesseract.AllOneClient.databinding.FragmentProfileBinding
import com.tesseract.AllOneClient.fragments.profile.addcard.getCards.GetCardViewModel
import com.tesseract.AllOneClient.utils.getNavOptions
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: GetCardViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this).get(GetCardViewModel::class.java)
        return binding.root
    }

    @SuppressLint("WrongConstant", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawerLayout: DrawerLayout =requireActivity().findViewById(R.id.drawerLayout)

        binding.drawerIcon.setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(Gravity.START)
            else drawerLayout.closeDrawer(Gravity.END)
            drawerLayout.openDrawer(Gravity.START)
        }

        binding.logOut.setOnClickListener {
            SaveData.loginUser(requireContext(), false)
            activity?.finish()
        }
        binding.apply {
            viewModel.getCardDataList(headerMapUniversal(requireContext()))

            viewModel.cardDataList.observe(requireActivity(), {

                if (it.isNotEmpty()){
                    binding.cardNumber.visibility=View.VISIBLE
                    val cardNumFormat=(it[0].cardNumber)?.replaceRange(6, 12, "******")
                    binding.cardNumber.text="${it[0].type} ${SaveData.formatCard(cardNumFormat!!)}"
                }else{
                    binding.cardNumber.visibility=View.GONE
                }

            })





            changeImage.setOnClickListener {
                openGallery()
            }

            bonusCard.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_bonusFragment2,
                    null,
                    getNavOptions()
                )
            }

            changeLanguage.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_changeLanguageFragment,
                    null,
                    getNavOptions()
                )
            }

            myCards.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_addCardFragment2,
                    null,
                    getNavOptions()
                )
            }

            technicalSupport.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_fragmentTechnicalSupport,
                    null,
                    getNavOptions()
                )
            }

            changeData.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_changeDataFragment,
                    null,
                    getNavOptions()
                )
            }

            changePhoneNum.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_changePhoneFragment,
                    null,
                    getNavOptions()
                )
            }

            aboutAppBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_profileFragment_to_aboutProgramFragment,
                    null,
                    getNavOptions()
                )
            }



            val number = SaveData.getPhone1(requireContext())
            binding.profileUserName.text= getName(requireContext())
            binding.profileUserPhoneNumber.text= number
            binding.phoneNumber.text=number
            binding.balanceText.text= getBalance(requireContext())

        }
    }


    private var PICK_IMAGE_INTENT = 1
    private fun openGallery() {

        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_INTENT)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                if (data!!.clipData == null) {
                    binding.imageProfile.setImageURI(data.data)
                    SaveData.saveProfileImage(requireContext(), data.data.toString())

                }
            }
        }
    }

}