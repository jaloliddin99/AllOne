package com.tesseract.AllOneClient.fragments.profile.settings.changePhone

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentChangePhoneNumberBinding
import com.tesseract.AllOneClient.fragments.login.confirmPhone.ConfirmPhoneFragmentDirections
import com.tesseract.AllOneClient.fragments.login.confirmPhone.ConfirmPhoneViewModel
import com.tesseract.AllOneClient.fragments.login.signIn.SignInViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePhoneFragment : Fragment() {

    private var _binding: FragmentChangePhoneNumberBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChangePhoneViewModel
    var phoneNumber: String = ""
    var isSent: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentChangePhoneNumberBinding.inflate(inflater, container, false)

        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangePhoneViewModel::class.java)



        binding.loader.loader.visibility=View.GONE

        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            codeField.addTextChangedListener(codeFieldWatcher)
            phoneNumberField.setText("+998")

            phoneNumberField.addTextChangedListener(textWatcher)

            sendNumber.setOnClickListener {
                phoneNumber = phoneNumberField.text.toString()
                if (!isSent) {
                    val isNotEmpty: Boolean =
                        viewModel.isNotEmpty(phoneNumberField.text.toString())
                    if (!isNotEmpty) {
                        phoneNumberField.error = getString(R.string.enter_your_phone)
                        return@setOnClickListener
                    }
                    binding.loader.loader.visibility=View.VISIBLE
                    viewModel.sendCode(headerMapUniversal(requireContext()), phoneNumber.replace(" ", ""))
                }
            }

            send.setOnClickListener {
                if (isSent) {
                    if (isFilled) {
                        binding.loader.loader.visibility=View.VISIBLE
                        viewModel.updatePhone(
                            headerMapUniversal(requireContext()),
                            phoneNumber.replace(" ", ""),
                            codeField.text.toString()
                        )
                    }
                }
            }

            viewModel.text.observe(requireActivity(),  {
                binding.loader.loader.visibility=View.GONE
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                if (it.equals("Ok")) {
                    desctiption.text =
                        getString(R.string._998_90_568_1015) + phoneNumber + " " + getString(R.string._88888888888888)
                    linearVisible0.visibility = View.GONE
                    linearVisible.visibility = View.VISIBLE
                    isSent = true


                }
            })

        }

        viewModel.userDetails.observe(requireActivity(),  {
            it.id.let { it1 ->
                if (it1 != null) {
                    SaveData.saveUserId(
                        requireContext(),
                        it1
                    )
                }
            }
            SaveData.savePhone1(requireContext(), phoneNumber)
            SaveData.savePhone(requireContext(), it.phone)
            SaveData.saveName(requireContext(), it.name)
            SaveData.saveGender(requireContext(), it.gender)
            SaveData.saveBirthdate(requireContext(), it.birthdate)
            SaveData.saveBalance(requireContext(), it.balance)
            SaveData.createdTime(requireContext(), it.created_at)

            binding.loader.loader.visibility=View.GONE
            findNavController().popBackStack()
        })

    }


    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 3 || start == 6 || start == 10 || start == 13) {
                if (binding.phoneNumberField.text.toString().replace(" ", "").length == 13) {
                    view?.hideKeyboard()
                }

            }
        }
    }

    private var isFilled: Boolean = false
    private val codeFieldWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (binding.codeField.text.toString().length == 6) {
                binding.send.backgroundTintList = requireContext().getColorStateList(R.color.green)
                view?.hideKeyboard()
                isFilled = true
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}