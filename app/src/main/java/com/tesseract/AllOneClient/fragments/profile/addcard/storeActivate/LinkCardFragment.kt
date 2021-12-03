package com.tesseract.AllOneClient.fragments.profile.addcard.storeActivate

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentLinkCardBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.newCardAdded
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkCardFragment : Fragment(R.layout.fragment_link_card) {

    var _fragmentLinkCardBinding: FragmentLinkCardBinding? = null
    val fragmentLinkCardBinding get() = _fragmentLinkCardBinding!!

    private var isSaveButtonEnabled:Boolean=false
    var cardId: Int = -1

    private lateinit var viewModel: StoreActivateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentLinkCardBinding= FragmentLinkCardBinding.inflate(inflater, container, false)

        return fragmentLinkCardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this).get(StoreActivateViewModel::class.java)

        fragmentLinkCardBinding.save.visibility=View.GONE
        fragmentLinkCardBinding.loader.loader.visibility=View.GONE
        fragmentLinkCardBinding.cardNumber.addTextChangedListener(textWatcher)
        fragmentLinkCardBinding.expirityDate.addTextChangedListener(expirityWatcher)
        fragmentLinkCardBinding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        getCode()
    }

    private fun getCode(){
        fragmentLinkCardBinding.apply {
            getCode.setOnClickListener {
                if (expirityDate.text.toString().length==5 && cardNumber.text.toString().length==19){
                    fragmentLinkCardBinding.loader.loader.visibility=View.VISIBLE
                    viewModel.storeCard(
                        headerMapUniversal(requireContext()),
                        cardName.text.toString(),
                        cardNumber.text.toString().replace(" ", ""),
                        expirityDate.text.toString().replace("/", "")
                    )
                }

            }

            codeField.addTextChangedListener(codeFieldWatcher)


            save.setOnClickListener {
                val code: String = codeField.text.toString()
                if (!isSaveButtonEnabled){
                    return@setOnClickListener
                }else{
                    Log.i("size ", ""+code+" "+code.length)
                    if (code.length==6){
                        fragmentLinkCardBinding.loader.loader.visibility=View.VISIBLE
                        viewModel.activateCard(
                            headerMapUniversal(requireContext()),
                            cardId,
                            code
                        )
                    }
                }
            }
        }


        viewModel.activateCardMsg.observe(requireActivity(), Observer {

            fragmentLinkCardBinding.loader.loader.visibility=View.GONE
            newCardAdded=true
            findNavController().popBackStack()
        })
        viewModel.errorMessageActiveCards.observe(requireActivity(), Observer {
            fragmentLinkCardBinding.loader.loader.visibility=View.GONE
            fragmentLinkCardBinding.incorrectCode.visibility=View.VISIBLE
            requireContext().getColor(R.color.red).let { it1 ->
                fragmentLinkCardBinding.codeField.setTextColor(
                    it1
                )
            }

        })

        viewModel.postStoreCardDataResponse.observe(requireActivity(), Observer {
            fragmentLinkCardBinding.save.visibility=View.VISIBLE
            fragmentLinkCardBinding.loader.loader.visibility=View.GONE
            cardId= it.id!!
            fragmentLinkCardBinding.apply {
                getCode.visibility=View.GONE
                linearLayout.visibility=View.VISIBLE
                save.alpha=1f
            }
            isSaveButtonEnabled=true


        })

        viewModel.errorMessage.observe(requireActivity(), Observer {
            fragmentLinkCardBinding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


    }

    private val expirityWatcher= object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 1) {
                fragmentLinkCardBinding.expirityDate.setText(fragmentLinkCardBinding.expirityDate.text.toString() + "/")
                fragmentLinkCardBinding.expirityDate.setSelection(fragmentLinkCardBinding.expirityDate.text.toString().length)
            }
            if (count == 2 && start == 0 && before == 1) {
                fragmentLinkCardBinding.expirityDate.setText(fragmentLinkCardBinding.expirityDate.text.toString().split("/").toTypedArray().get(0))

            }
            Log.i("counter ", ""+count)
            if (fragmentLinkCardBinding.expirityDate.text.toString().length==5){
                view?.hideKeyboard()
            }

        }


        override fun afterTextChanged(s: Editable?) {

        }

    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (start == 3 || start == 8 || start == 13) {
                fragmentLinkCardBinding.cardNumber.setText(fragmentLinkCardBinding.cardNumber.text.toString() + " ")
                fragmentLinkCardBinding.cardNumber.setSelection(fragmentLinkCardBinding.cardNumber.text.toString().length)
            }
            if (count == 14 && start == 0 && before == 13 || count == 9 && start == 0 && before == 8 || count == 4 && start == 0 && before == 3) {
                fragmentLinkCardBinding.cardNumber.setText(fragmentLinkCardBinding.cardNumber.text.toString().trim { it <= ' ' })
            }

        }
    }

    private val codeFieldWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (fragmentLinkCardBinding.codeField.text.toString().length == 6) {
                view?.hideKeyboard()
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
        _fragmentLinkCardBinding=null
    }



}