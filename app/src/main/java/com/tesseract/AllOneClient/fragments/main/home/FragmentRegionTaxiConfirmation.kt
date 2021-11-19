package com.tesseract.AllOneClient.fragments.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentRegionTaxiConfirmationBinding

class FragmentRegionTaxiConfirmation: Fragment(R.layout.fragment_region_taxi_confirmation) {
    private var _binding: FragmentRegionTaxiConfirmationBinding? = null
    private val binding get() = _binding!!
    private val time= 20_000L


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentRegionTaxiConfirmationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            val timer = object: CountDownTimer(time, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("context ", ""+millisUntilFinished)
                    val progress=(1-millisUntilFinished.toFloat()/time.toFloat())*100
                    Log.i("contextdwdd ", ""+progress)
                    wrongProgress.progress= progress.toFloat()

                }

                override fun onFinish() {
                    wrongProgress.progress=100f
                }
            }
            timer.start()
        }

        binding.bookNow.setOnClickListener {
            val action= FragmentRegionTaxiConfirmationDirections.actionFragmentRegionTaxiConfirmationToFragmentRegionTaxiConfirmation2()
            findNavController().navigate(action)
        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}