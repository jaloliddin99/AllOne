package com.tesseract.AllOneClient.dialogs.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogMainStandartShareBinding

class StandardShare(
    private val image: Int,
    val title:String,
    val description:String,
    private val listener: OnDialogClickListaner
    ) : DialogFragment(R.layout.dialog_main_standart_share){

    private var binding: DialogMainStandartShareBinding?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dialog_main_standart_share, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainStandartShareBinding=DialogMainStandartShareBinding.bind(view)
        binding=mainStandartShareBinding

        binding?.carImage?.setImageResource(image)
        binding?.title?.text=title
        binding?.description?.text=description



        binding?.continueButton?.setOnClickListener{
            listener.onDialogClick()
            dialog?.dismiss()
        }

    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun show(context: Context, s: String) {
        dialog?.show()
    }

    interface OnDialogClickListaner{
        fun onDialogClick()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }
}