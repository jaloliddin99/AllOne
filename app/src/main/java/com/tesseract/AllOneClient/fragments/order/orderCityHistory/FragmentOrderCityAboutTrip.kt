package com.tesseract.AllOneClient.fragments.order.orderCityHistory

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
import com.tesseract.AllOneClient.databinding.FragmentOrderCityAboutTripBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrderCityAboutTrip: Fragment() {
    private var _binding: FragmentOrderCityAboutTripBinding?=null
    private val binding get() = _binding!!
    private val args: FragmentOrderCityAboutTripArgs by navArgs()
    var fromLatlng:String=""
    var toLatlng:String=""
    private lateinit var viewModel: OrderCityHistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentOrderCityAboutTripBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(OrderCityHistoryViewModel::class.java)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getOrderCityHistory(headerMapUniversal(requireContext()), args.orderId)

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })
        viewModel.getOrderCityHistory.observe(requireActivity(), {

            binding.loader.loader.visibility=View.GONE
            binding.amount.text=it.amount+" "+getString(R.string.summa1)
            binding.bonusAmount.text=it.bonus_amount+" "+getString(R.string.summa1)
            binding.time.text=it.time
            binding.id.text= it.id.toString()
            binding.distance.text=it.distance
            binding.driverName.text=it.driver_name

            binding.paymentType.text=it.payment_type
            binding.tariff.text=it.tariff

            driverId= it.driver_id
            orderId=it.id
            orderType=it.order_type

            binding.startPoint.text=it.point_names[0]
            binding.endPoint.text=it.point_names[it.point_names.lastIndex]

        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signIn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toDriverFragment.setOnClickListener {
            val action= FragmentOrderCityAboutTripDirections.actionGlobalAboutDriver(driverId, args.orderId, orderType)
            findNavController().navigate(action)
        }

    }

    private var orderId:Int=-12
    private var orderType=""
    private var driverId:Int=-11
}