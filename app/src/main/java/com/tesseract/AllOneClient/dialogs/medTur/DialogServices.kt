package com.tesseract.AllOneClient.dialogs.medTur

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicPriceAdapter
import com.tesseract.AllOneClient.databinding.DialogClinicServicesBinding
import com.tesseract.AllOneClient.model.medTourism.clinicServices.Service

class DialogServices(private val services: Service) : BottomSheetDialogFragment() {
    private var binding: DialogClinicServicesBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogClinicServicesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            cancel.setOnClickListener {
                dialog?.dismiss()

            }
            name.text=services.name
            price.text=services.price
            description.text=services.description

            recyclerView.apply {
                layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter= ClinicPriceAdapter(services.price_list)
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

}