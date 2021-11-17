package com.tesseract.AllOneClient.dialogs.login

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogShowDayBinding

class DialogDateOfBirth(
    private val title: String,
    private var dayListener: OnDaySelectListener): DialogFragment(R.layout.dialog_show_day) {

    private var binding: DialogShowDayBinding?=null
    private var date: String=""
    private var  isChanged=false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_show_day, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val showTimeBinding= DialogShowDayBinding.bind(view)

        binding=showTimeBinding

        binding?.cancelImg?.setOnClickListener {
            dialog?.dismiss()
        }

        binding?.title?.text=title

        binding?.confirmDay?.setOnClickListener {
            if (isChanged){
                dayListener.selectDayListener(date)
            }else{
                var day: String = ""
                var month: String = ""
                val dayOfMonth=binding?.datePicker1?.dayOfMonth
                val monthOfYear=binding?.datePicker1?.month
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

                date= "$day.$month.${binding?.datePicker1?.year}"
                dayListener.selectDayListener(date)
            }
            dialog?.dismiss()
        }

        binding?.datePicker1?.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            isChanged=true
            var day: String = ""
            var month: String = ""
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

            date= "$day.$month.$year"

        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

    interface OnDaySelectListener{
        fun selectDayListener(time: String)
    }
    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}