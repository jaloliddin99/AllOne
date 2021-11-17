package com.tesseract.AllOneClient.fragments.order.orderRegionHistory

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentOrderRegionAboutTripBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrderRegionAboutTrip: Fragment() {

    private val args: FragmentOrderRegionAboutTripArgs by navArgs()
    var binding: FragmentOrderRegionAboutTripBinding?=null
    private lateinit var viewModel: RegionAboutTripViewModel
    lateinit var dialog: Dialog
    private var driverId:Int=0
    var fromLatlng:String=""
    var toLatlng:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentOrderRegionAboutTripBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(RegionAboutTripViewModel::class.java)
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loader()
        viewModel.getTaxiOrderHistory(headerMapUniversal(requireContext()), args.id)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.signIn?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.toDriverFragment?.setOnClickListener {
            val action=FragmentOrderRegionAboutTripDirections.actionGlobalAboutDriver(driverId, orderId, orderType)
            findNavController().navigate(action)
        }

        viewModel.historyView.observe(requireActivity(), Observer {
            dialog.dismiss()
            binding?.amount?.text=it?.amount+context?.getString(R.string.emptySpace)+context?.getString(R.string.summa1)
            binding?.bonusAmount?.text=it?.bonusAmount+it?.amount+context?.getString(R.string.emptySpace)+context?.getString(R.string.summa1)
            binding?.date?.text=it?.time
            binding?.distance?.text=it?.distance
            binding?.driverName?.text=it?.driverName
            binding?.from?.text=it?.from
            binding?.paymentType?.text=it?.paymentType
            binding?.tariff?.text=it?.tariff
            binding?.title?.text=it?.title
            binding?.to?.text=it?.to
            driverId= it?.driverId!!
            fromLatlng= it.fromLatLng.toString()
            toLatlng=it.toLatLng.toString()
            orderId=it.id!!
            orderType=it.orderType!!

        })

        binding?.mapFrom?.setOnClickListener {
            val action= FragmentOrderRegionAboutTripDirections.actionGlobalShowFromMap(fromLatlng)
            findNavController().navigate(action)
        }
        binding?.mapTo?.setOnClickListener {
            val action= FragmentOrderRegionAboutTripDirections.actionGlobalShowFromMap(toLatlng)
            findNavController().navigate(action)
        }

        viewModel.errorCatch.observe(requireActivity(), Observer {
            dialog.dismiss()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

    }

    private var orderId:Int=-1
    private var orderType=""

    private fun loader(){
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}