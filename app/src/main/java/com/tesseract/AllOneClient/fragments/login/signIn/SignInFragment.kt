package com.tesseract.AllOneClient.fragments.login.signIn

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentSignInBinding
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_in.*

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var binding: FragmentSignInBinding? = null

    private lateinit var viewModel: SignInViewModel
    var phoneNumber: String=""

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signInBinding = FragmentSignInBinding.bind(view)
        binding = signInBinding
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )

        binding?.txtSignIn?.setOnTouchListener(OnTouchListener { _, _ ->
            if (binding?.txtSignIn?.text?.isEmpty()==true){
                binding?.txtSignIn?.setText("+998 ")
                binding?.txtSignIn?.text?.length?.let { binding?.txtSignIn?.setSelection(it) }
            }
            false
        })


        binding?.signIn?.setOnClickListener {
            val isNotEmpty: Boolean = viewModel.isNotEmpty(binding?.txtSignIn?.text.toString())
            if (!isNotEmpty) {
                binding?.txtSignIn?.error = getString(R.string.enter_your_phone)
                return@setOnClickListener
            }

            phoneNumber = binding?.txtSignIn?.text.toString()
                .replace("(", "")
                .replace(")", "")

            viewModel.saveDetails(phoneNumber.replace(" ", ""))
        }

        viewModel.text.observe(requireActivity(), {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            if (it.equals("Ok")){
                if (SaveData.isSignInFragment){
                    val action =
                        SignInFragmentDirections.actionSignInFragmentToConfirmPhoneFragment(
                            phoneNumber
                        )
                    SaveData.isSignInFragment=false
                    findNavController().navigate(action)
                }else {
                    SaveData.isSignInFragment=true
                }

            }else{
                Toast.makeText(context, "bomadi", Toast.LENGTH_LONG).show()
            }
        })
        binding?.txtSignIn?.addTextChangedListener(textWatcher)
        binding?.txtSignIn?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (binding?.txtSignIn?.text.toString().endsWith(" ")){
                        binding?.txtSignIn?.setText(binding?.txtSignIn?.text?.trim())
                        binding?.txtSignIn?.text?.length?.let { binding?.txtSignIn?.setSelection(it) }
                    }
                }
                return false
            }
        })
    }



    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }
        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 3 ||start == 6 || start == 10|| start == 13) {
                binding?.txtSignIn?.setText(binding!!.txtSignIn.text.toString() + " ")
                binding?.txtSignIn?.setSelection(binding?.txtSignIn?.text.toString().length)
            }
        }
    }

    fun showKeyboard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }


}