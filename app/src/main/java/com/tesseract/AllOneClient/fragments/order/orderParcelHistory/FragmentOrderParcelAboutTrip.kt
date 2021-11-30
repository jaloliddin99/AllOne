package com.tesseract.AllOneClient.fragments.order.orderParcelHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.databinding.FragmentOrderAboutParcelBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrderParcelAboutTrip: Fragment() {
    private var _binding: FragmentOrderAboutParcelBinding?=null
    private val binding get() = _binding!!
    private val args: FragmentOrderParcelAboutTripArgs by navArgs()
    private lateinit var viewModel: OrderParcelViewModel
    private var driverId : Int=0
    var fromLatlng:String=""
    var toLatlng:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderAboutParcelBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(OrderParcelViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getParcelDeliveryOrder(headerMapUniversal(requireContext()), args.id)

        viewModel.orderParcelATData.observe(requireActivity(),  {
            binding.loader.loader.visibility=View.GONE
            binding.amount.text=it.amount
            binding.bonusAmount.text=it.bonusAmount
            binding.date.text=it.date
            binding.distance.text=it.distance
            binding.driverName.text=it.driverName
            binding.from.text=it.from
            binding.paymentType.text=it.paymentType
            binding.tariff.text=it.tariff
            binding.title.text=it.title
            binding.to.text=it.to
            binding.receiverName.text=it.receiver
            binding.parcelType.text=it.parcelType
            driverId= it.driverId!!
            fromLatlng= it.fromLatLng.toString()
            toLatlng=it.toLatLng.toString()
            orderId=it.id!!
            orderType=it.orderType!!

        })

        viewModel.errorMessage.observe(requireActivity(),  {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signIn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toDriverFragment.setOnClickListener {
            val action=FragmentOrderParcelAboutTripDirections.actionGlobalAboutDriver(driverId, orderId, orderType)
            findNavController().navigate(action)
        }

        binding.mapFrom.setOnClickListener {
            val action=FragmentOrderParcelAboutTripDirections.actionGlobalShowFromMap(fromLatlng)
            findNavController().navigate(action)
        }
        binding.mapTo.setOnClickListener {
            val action=FragmentOrderParcelAboutTripDirections.actionGlobalShowFromMap(toLatlng)
            findNavController().navigate(action)
        }

    }

    private var orderId:Int=-1
    private var orderType=""

}