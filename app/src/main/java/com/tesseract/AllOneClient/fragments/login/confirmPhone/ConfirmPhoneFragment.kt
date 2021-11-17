package com.tesseract.AllOneClient.fragments.login.confirmPhone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.isConfirmFragmnet
import com.tesseract.AllOneClient.constants.SaveData.savePhone1
import com.tesseract.AllOneClient.databinding.FragmentConfirmPhoneBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmPhoneFragment: Fragment(R.layout.fragment_confirm_phone) {

    val args: ConfirmPhoneFragmentArgs by navArgs()

    private var binding: FragmentConfirmPhoneBinding?=null
    private lateinit var viewModel: ConfirmPhoneViewModel

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmPhoneBinding=FragmentConfirmPhoneBinding.bind(view)

        binding=confirmPhoneBinding
        viewModel=ViewModelProvider(this).get(ConfirmPhoneViewModel::class.java)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        val number=args.phone.replace(" ", "")

        binding?.description?.text=getString(R.string._998_90_568_1015)+args.phone+" "+getString(R.string._88888888888888)

        binding?.confirmButton?.setOnClickListener{

            if (binding?.otpView?.text.toString().length!=6){
                return@setOnClickListener
            }
            viewModel.loginUser(number, binding?.otpView?.text.toString())

        }

        viewModel.textPhone.observe(requireActivity(), Observer {
            if (it.equals("registered")){

                savePhone1(requireContext(), args.phone)
                activity?.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }
                SaveData.loginUser(requireContext(), true)
                activity?.finish()
            }
            if (it.equals("not_registered")){
                savePhone1(requireContext(), args.phone)
                if (isConfirmFragmnet){
                    val action=ConfirmPhoneFragmentDirections.actionConfirmPhoneFragmentToRegisterFragment(args.phone)
                    isConfirmFragmnet=false
                    findNavController().navigate(action)
                }else{
                    isConfirmFragmnet=true
                }



            }
        })

        viewModel.userToken.observe(requireActivity(), Observer {
            SaveData.addData(requireContext(), it)
            Log.i("user token", ""+it)
        })
        viewModel.userDetails.observe(requireActivity(), Observer {
            it.id?.let { it1 ->
                SaveData.saveUserId(
                    requireContext(),
                    it1
                )
            }
            SaveData.savePhone(requireContext(), it.phone)
            SaveData.saveName(requireContext(), it.name)
            SaveData.saveGender(requireContext(), it.gender)
            SaveData.saveBirthdate(requireContext(), it.birthdate)
            SaveData.saveBalance(requireContext(), it.balance)
            SaveData.createdTime(requireContext(), it.created_at)
        })


    }



}