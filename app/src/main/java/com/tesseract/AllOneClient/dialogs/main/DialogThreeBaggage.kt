package com.tesseract.AllOneClient.dialogs.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogBaggageInfoBinding

class DialogThreeBaggage(val title:String, val size: String, val mass:String, val price:String): DialogFragment(R.layout.dialog_baggage_info) {

    private var binding: DialogBaggageInfoBinding?=null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NO_TITLE, R.style.DialogAnimation)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_baggage_info, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val baggageInfoBinding= DialogBaggageInfoBinding.bind(view)
        binding=baggageInfoBinding

        binding?.title?.text=title
        binding?.mass?.text=mass
        binding?.size?.text=size
        binding?.price?.text=price

        binding?.cancelImage?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.cancelButton?.setOnClickListener {
            dialog?.dismiss()
        }

    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

}