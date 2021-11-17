package com.tesseract.AllOneClient.dialogs.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogCityOrderFinishedBinding


class CityOrderFinished(val listener: OnLickListener): BottomSheetDialogFragment() {
    private var binding:DialogCityOrderFinishedBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCityOrderFinishedBinding.inflate(inflater, container, false)

        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val lyout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet!!.height
            lyout.parent.requestLayout()
        }
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.close?.setOnClickListener {
            dialog?.dismiss()
            listener.rate()
        }

        binding?.aboutTheTrip?.setOnClickListener {
            dialog?.dismiss()
            listener.aboutTrip()
        }


    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

    interface OnLickListener{
        fun rate()
        fun aboutTrip()
    }

}