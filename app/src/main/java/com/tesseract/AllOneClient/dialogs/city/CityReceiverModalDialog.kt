package com.tesseract.AllOneClient.dialogs.city

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogCityReceiverModalDialogBinding
import kotlinx.android.synthetic.main.layout_city_contact.*

class CityReceiverModalDialog(private val listener:OnOrderClickListener):BottomSheetDialogFragment() {
    private lateinit var binding: DialogCityReceiverModalDialogBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DialogCityReceiverModalDialogBinding.inflate(inflater, container, false)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val lyout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet!!.height
            lyout.parent.requestLayout()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            order.setOnClickListener {
                if (receiverName.text.toString().isEmpty()){
                    return@setOnClickListener
                }
                if (receiverComment.text.toString().isEmpty()){
                    return@setOnClickListener
                }

                listener.receiverDetails(receiverName.text.toString(), receiverComment.text.toString())
                dialog?.dismiss()
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun AppCompatEditText.onRightDrawableClicked(onClicked: (view: AppCompatEditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is AppCompatEditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

    interface OnOrderClickListener{
        fun receiverDetails(phoneNum:String, receiverComment:String)
    }

}