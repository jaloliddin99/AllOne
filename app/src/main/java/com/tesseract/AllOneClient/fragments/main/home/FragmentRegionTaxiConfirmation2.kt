package com.tesseract.AllOneClient.fragments.main.home

import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentRegionTaxiConfirmation2Binding

class FragmentRegionTaxiConfirmation2: Fragment(R.layout.fragment_region_taxi_confirmation2) {
    private var binding: FragmentRegionTaxiConfirmation2Binding? =null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val regionTaxiConfirmation2Binding=FragmentRegionTaxiConfirmation2Binding.bind(view)

        binding=regionTaxiConfirmation2Binding
        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }


//        if (args.fromWhichLayout==1){
//            binding?.gonneableView?.visibility=View.VISIBLE
//            binding?.gonneableView1?.visibility=View.VISIBLE
//            binding?.post?.visibility=View.GONE
//            binding?.rubbish?.setImageResource(R.drawable.ic_car_icon)
//            binding?.region?.visibility=View.VISIBLE
//            binding?.orderType?.setImageResource(R.drawable.ic_white_car)
//            binding?.metka?.setImageResource(R.drawable.ic_metka)
//        }else if (args.fromWhichLayout==0){
//            binding?.gonneableView?.visibility=View.GONE
//            binding?.gonneableView1?.visibility=View.GONE
//            binding?.post?.visibility=View.VISIBLE
//            binding?.region?.visibility=View.GONE
//            binding?.rubbish?.setImageResource(R.drawable.ic_rubbush)
//            binding?.metka?.setImageResource(R.drawable.ic_calendar)
//            binding?.orderType?.setImageResource(R.drawable.ic_inter_nation_and_city)
//        }


        binding?.bookNow?.setOnClickListener {

            val action =FragmentRegionTaxiConfirmation2Directions.actionGlobalInterareaActiveOrder(7, "standard")
            findNavController().navigate(action)

        }


    }
}