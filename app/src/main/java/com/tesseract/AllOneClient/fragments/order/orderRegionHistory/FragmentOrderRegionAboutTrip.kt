package com.tesseract.AllOneClient.fragments.order.orderRegionHistory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    var _binding: FragmentOrderRegionAboutTripBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegionAboutTripViewModel
    private var driverId:Int=0
    var fromLatlng:String=""
    var toLatlng:String=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderRegionAboutTripBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(RegionAboutTripViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTaxiOrderHistory(headerMapUniversal(requireContext()), args.id)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signIn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toDriverFragment.setOnClickListener {
            val action=FragmentOrderRegionAboutTripDirections.actionGlobalAboutDriver(driverId, orderId, orderType)
            findNavController().navigate(action)
        }

        viewModel.historyView.observe(requireActivity(), {

            binding.loader.loader.visibility=View.GONE
            binding.amount.text=it.amount+requireContext().getString(R.string.emptySpace)+requireContext().getString(R.string.summa1)
            binding.bonusAmount.text=it.bonusAmount+it.amount+requireContext().getString(R.string.emptySpace)+requireContext().getString(R.string.summa1)
            binding.date.text=it.time
            binding.distance.text=it.distance
            binding.driverName.text=it.driverName
            binding.from.text=it.from
            binding.paymentType.text=it.paymentType
            binding.tariff.text=it.tariff
            binding.title.text=it.title
            binding.to.text=it.to
            driverId= it.driverId!!
            fromLatlng= it.fromLatLng.toString()
            toLatlng=it.toLatLng.toString()
            orderId=it.id!!
            orderType=it.orderType!!

        })

        binding.mapFrom.setOnClickListener {
            val action= FragmentOrderRegionAboutTripDirections.actionGlobalShowFromMap(fromLatlng)
            findNavController().navigate(action)
        }
        binding.mapTo.setOnClickListener {
            val action= FragmentOrderRegionAboutTripDirections.actionGlobalShowFromMap(toLatlng)
            findNavController().navigate(action)
        }

        viewModel.errorCatch.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

    }

    private var orderId:Int=-1
    private var orderType=""


}