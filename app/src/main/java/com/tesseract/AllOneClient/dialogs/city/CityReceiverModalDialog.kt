package com.tesseract.AllOneClient.dialogs.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogCityReceiverModalDialogBinding
import kotlinx.android.synthetic.main.layout_city_contact.*

class CityReceiverModalDialog(private val listener:OnOrderClickListener):BottomSheetDialogFragment() {
    private var _binding: DialogCityReceiverModalDialogBinding?=null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=DialogCityReceiverModalDialogBinding.inflate(inflater, container, false)
        dialog!!.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val lyout = bottomSheet!!.parent as CoordinatorLayout
            val behavior: BottomSheetBehavior<*> =
                BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = bottomSheet.height
            lyout.parent.requestLayout()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            receiverName.setText("+998 ")
            order.setOnClickListener {
                if (receiverName.text.toString().length!=17){
                    Toast.makeText(
                        context,
                        "Please, enter correct phone number",
                        Toast.LENGTH_SHORT
                    ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}