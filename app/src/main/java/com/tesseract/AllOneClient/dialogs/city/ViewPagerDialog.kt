package com.tesseract.AllOneClient.dialogs.city

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogCityTariffDisplayBinding
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Content


class ViewPagerDialog(private val content: Content): DialogFragment(){

    private lateinit var binding:DialogCityTariffDisplayBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=DialogCityTariffDisplayBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background)
        isCancelable = false
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cancel.setOnClickListener {
                dialog?.dismiss()
            }

            tariff.text=content.tariff
            carName.text=content.cars
            Picasso.get().load(content.img).into(carImage)
            airConditioner.text=content.ac.toString()
            info.text=content.info.replace("\\n", "\n")

        }


    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
    }



    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}