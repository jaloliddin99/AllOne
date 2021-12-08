package com.tesseract.AllOneClient.dialogs.parcel

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
import com.tesseract.AllOneClient.databinding.DialogModelPostTypeSelectionBinding

class ModalDialogParcelSelection(private val listener: ClickListener) : BottomSheetDialogFragment() {

    private var binding:  DialogModelPostTypeSelectionBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= DialogModelPostTypeSelectionBinding.inflate(inflater, container, false)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val lyout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet.height
            lyout.parent.requestLayout()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.city?.setOnClickListener {
            listener.parcelType(1)
            dialog?.dismiss()
        }
        binding?.region?.setOnClickListener {
            listener.parcelType(2)
            dialog?.dismiss()
        }
        binding?.international?.setOnClickListener {
            listener.parcelType(3)
            dialog?.dismiss()
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }


    interface ClickListener{
        fun parcelType(position:  Int)
    }

}