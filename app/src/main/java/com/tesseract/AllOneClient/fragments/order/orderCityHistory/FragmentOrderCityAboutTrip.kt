package com.tesseract.AllOneClient.fragments.order.orderCityHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.databinding.FragmentOrderCityAboutTripBinding

class FragmentOrderCityAboutTrip: Fragment() {
    var binding: FragmentOrderCityAboutTripBinding?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentOrderCityAboutTripBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.signIn?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.toDriverFragment?.setOnClickListener {
            val action= FragmentOrderCityAboutTripDirections.actionGlobalAboutDriver(1, 2, "city")
            findNavController().navigate(action)
        }

    }
}