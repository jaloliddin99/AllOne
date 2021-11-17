package com.tesseract.AllOneClient.dialogs.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.DialogDeleteCardBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDeleteCard(private val cardId: Int,
                       private val cardName:String,
                       private val cardExp:String,
                       private val cardNum:String,
                       private val listener: DeleteListener
                       )
    : DialogFragment() {
    lateinit var dialog1: Dialog
    var binding: DialogDeleteCardBinding?=null
    private lateinit var viewModel: DeleteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DialogDeleteCardBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        viewModel=ViewModelProvider(this).get(DeleteViewModel::class.java)
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding?.cardName?.text=context?.getString(R.string.card)+" "+cardName
        binding?.cardNumber?.text=cardNum
        binding?.expDate?.text=SaveData.formatExpDate(cardExp)

        binding?.cancel?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.delete?.setOnClickListener {
            loader()
            viewModel.deleteCard(headerMapUniversal(requireContext()), cardId)
        }

        viewModel.successM.observe(requireActivity(), {
            dialog1.dismiss()
            dialog?.dismiss()
            listener.deleted()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.errorM.observe(requireActivity(), {
            dialog1.dismiss()
            dialog?.dismiss()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

    }

    private fun loader(){
        dialog1 = Dialog(requireActivity())
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.loader)
        dialog1.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog1.show()
    }

    interface DeleteListener{
        fun deleted()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}