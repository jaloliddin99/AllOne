package com.tesseract.AllOneClient.fragments.login.confirmPhone

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.isConfirmFragmnet
import com.tesseract.AllOneClient.constants.SaveData.savePhone1
import com.tesseract.AllOneClient.databinding.FragmentConfirmPhoneBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ConfirmPhoneFragment: Fragment(R.layout.fragment_confirm_phone) {

    val args: ConfirmPhoneFragmentArgs by navArgs()

    private var _binding: FragmentConfirmPhoneBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: ConfirmPhoneViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentConfirmPhoneBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this).get(ConfirmPhoneViewModel::class.java)


        val number=args.phone.replace(" ", "")
        binding.loader.loader.visibility=View.GONE
        binding.description.text=getString(R.string._998_90_568_1015)+" "+args.phone+" "+getString(R.string._88888888888888)

        binding.confirmButton.setOnClickListener{

            if (binding.otpView.text.toString().length!=6){
                return@setOnClickListener
            }
            binding.loader.loader.visibility=View.VISIBLE
            viewModel.loginUser(number, binding.otpView.text.toString())

        }

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })

        binding.otpView.addTextChangedListener(textWatcher)



        val countDownTimer:CountDownTimer=object :CountDownTimer(120000L, 1000){
            override fun onTick(l: Long) {

                binding.countdownTimer.text=getString(R.string.codeIsSentIn)+" "+String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(l), TimeUnit.MILLISECONDS.toSeconds(l)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
                )
            }

            override fun onFinish() {
                try {
                    binding.countdownTimer.text=getString(R.string.sendAgain)
                    binding.countdownTimer.setTextColor(requireContext().getColor(R.color.green))
                }catch (e:Exception){

                }
            }

        }
        countDownTimer.start()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    countDownTimer.cancel()
                    findNavController().popBackStack()

                }
            })


        binding.backToHome.setOnClickListener {
            countDownTimer.cancel()
            findNavController().popBackStack()
        }

        viewModel.textPhone.observe(requireActivity(), {
            countDownTimer.cancel()
            binding.loader.loader.visibility=View.GONE
            if (it.equals("registered")){
                savePhone1(requireContext(), args.phone)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                requireActivity().startActivity(intent)
                SaveData.loginUser(requireContext(), true)
                requireActivity().finish()
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




        viewModel.userToken.observe(requireActivity(),  {
            SaveData.addData(requireContext(), it)
        })
        viewModel.userDetails.observe(requireActivity(),  {
            it.id.let { it1 ->
                if (it1 != null) {
                    SaveData.saveUserId(
                        requireContext(),
                        it1
                    )
                }
            }
            SaveData.savePhone(requireContext(), it.phone)
            SaveData.saveName(requireContext(), it.name)
            SaveData.saveGender(requireContext(), it.gender)
            SaveData.saveBirthdate(requireContext(), it.birthdate)
            SaveData.saveBalance(requireContext(), it.balance)
            SaveData.createdTime(requireContext(), it.created_at)
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding=null
    }


    val textWatcher=object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
           if (s?.length==6){
               binding.confirmButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.green))
               view?.hideKeyboard()
           }else{
               binding.confirmButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.dark_grey))
           }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}