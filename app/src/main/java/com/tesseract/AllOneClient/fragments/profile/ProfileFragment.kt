package com.tesseract.AllOneClient.fragments.profile

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.getBalance
import com.tesseract.AllOneClient.constants.SaveData.getName
import com.tesseract.AllOneClient.databinding.FragmentProfileBinding
import com.tesseract.AllOneClient.utils.getNavOptions
import java.io.File


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var fragmentProfile: FragmentProfileBinding?=null

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding=FragmentProfileBinding.bind(view)
        fragmentProfile=binding
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.logOut.setOnClickListener {
            SaveData.loginUser(requireContext(), false)
            activity?.finish()
        }
        fragmentProfile?.apply {

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
            val balance:String="22000999488"
            binding.balanceText.text= getBalance(requireContext())

        }

        //setAudioStart()


    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun myImage(){
        val mySongs = getImageGallery(Environment.getExternalStorageDirectory())
        for (i in 0 until mySongs!!.size) {
            Log.d("222", "onViewCreated: ${i}")
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun setAudioStart() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(requireActivity(), permissions, 0)
        } else {
            myImage()
        }
    }


    private fun getImageGallery(root: File): ArrayList<File>? {
        val al: ArrayList<File> = ArrayList()
        val files: Array<File> = root.listFiles()
        for (singeFile in files) {
            if (singeFile.isDirectory && !singeFile.isHidden) {
                al.addAll(getImageGallery(singeFile)!!)
            } else {
                if (singeFile.name.endsWith(".png") || singeFile.name.endsWith(".jpg")) {
                    al.add(singeFile)
                }
            }
        }
        return al
    }



    var mediaUriList = ArrayList<String>()
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
                    mediaUriList.add(data.data.toString())
                    fragmentProfile?.imageProfile?.setImageURI(data.data)

                }
            }
        }
    }

}