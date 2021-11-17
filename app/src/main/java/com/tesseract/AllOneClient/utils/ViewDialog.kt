package com.tesseract.AllOneClient.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import com.tesseract.AllOneClient.R

class ViewDialog {
    lateinit var dialog: Dialog
    fun showDialog(activity: Activity?, isShow:Boolean) {
        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (isShow){
            Log.i("show dialog", "hello")
            dialog.show()
        }else{
            Log.i("cancel dialog", "hello")
            dialog.dismiss()
        }

    }
}