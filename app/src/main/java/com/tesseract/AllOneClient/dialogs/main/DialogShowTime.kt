package com.tesseract.AllOneClient.dialogs.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogShowDayBinding

class DialogShowTime(
    private val title: String,
    private var dayListener: OnDaySelectListener): DialogFragment(R.layout.dialog_show_day) {

    private var _binding: DialogShowDayBinding?=null
    private val binding get() = _binding!!
    private var date: String=""
    private var  isChanged=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= DialogShowDayBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelImg.setOnClickListener {
            dialog?.dismiss()
        }

        binding.title.text=title

        binding.confirmDay.setOnClickListener {
            if (isChanged){
                dayListener.selectDayListener(date)
            }else{
                var day = ""
                val month: String
                val dayOfMonth= binding.datePicker1.dayOfMonth

                val someValue=binding.datePicker1.month.toString().toInt()
                val monthOfYear:Int= someValue +1

                day = if (dayOfMonth in 0..9) {
                    "0$dayOfMonth"
                } else {
                    "$dayOfMonth"
                }

                month = if (monthOfYear in 0..9) {
                    "0$monthOfYear"
                } else {
                    "$monthOfYear"
                }

                date= "$day.$month.${binding.datePicker1.year}"
                dayListener.selectDayListener(date)
            }
            dialog?.dismiss()
        }

        binding.datePicker1.minDate=System.currentTimeMillis()-1000
        binding.datePicker1.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            isChanged=true
            val day: String = if (dayOfMonth in 0..9) {
                "0$dayOfMonth"
            } else {
                "$dayOfMonth"
            }

            val month: String = if ((monthOfYear+1) in 0..9) {
                "0${(monthOfYear+1)}"
            } else {
                "${(monthOfYear+1)}"
            }

            date= "$day.$month.$year"

        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations  = R.style.DialogAnimation;
    }

    interface OnDaySelectListener{
        fun selectDayListener(time: String)
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}