package com.tesseract.AllOneClient.dialogs.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogUserGenderBinding
import com.tesseract.AllOneClient.utils.xValue
import com.tesseract.AllOneClient.utils.yValue


class DialogPoll(private val onSelectListener: OnSelectListener) : DialogFragment(R.layout.dialog_user_gender){


    private var binding: DialogUserGenderBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_user_gender, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.background_grey);
        val wmlp = dialog!!.window!!.attributes

        wmlp.x= xValue.toInt()
        wmlp.y= yValue.toInt()

        Toast.makeText(context, "$xValue, $yValue", Toast.LENGTH_SHORT).show()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userGenderBinding=DialogUserGenderBinding.bind(view)

        binding=userGenderBinding

        binding?.female?.setOnClickListener {
            onSelectListener.userGender(getString(R.string.female), 2)
            dialog?.dismiss()
        }

        binding?.male?.setOnClickListener {
            onSelectListener.userGender(getString(R.string.male), 1)
            dialog?.dismiss()
        }


    }
    interface OnSelectListener{
        fun userGender(gender: String, id:Int)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


}

