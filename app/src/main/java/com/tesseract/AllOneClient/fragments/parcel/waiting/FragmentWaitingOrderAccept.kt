package com.tesseract.AllOneClient.fragments.parcel.waiting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentParcelWaitingOrderAcceptBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.parcel.selectLocation.SelectLocationViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentWaitingOrderAccept : Fragment() {


    private lateinit var viewModel:SelectLocationViewModel
    val args: FragmentWaitingOrderAcceptArgs by navArgs()
    private lateinit var binding: FragmentParcelWaitingOrderAcceptBinding
    private val shareViewModel: ShareDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParcelWaitingOrderAcceptBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            bottomSHeet.orderId.text = args.parcelOrderId.toString()
            bottomSHeet.orderId2.text = "№" + args.parcelOrderId.toString()

            cancel.setOnClickListener {
                val action=FragmentWaitingOrderAcceptDirections.actionFragmentWaitingOrderAcceptToFragmentWaitingSuccess(args.parcelOrderId)
                findNavController().navigate(action)
            }

            viewModel.data.observe(viewLifecycleOwner, {
                bottomSHeet.location.text=it.region
            })
        }
        shareModel()



    }

    private var parcelStartId = -1
    private var parcelEndId = -1
    private var parcelDepDate = ""
    private var parcelLocation = ""
    private var parcelReceiverName = ""
    private var parcelReceiverPhoneNumber = ""
    private var parcelBaggage = ""
    private var parcelBaggagePlaces = ""
    private var parcelPaymentType = ""
    private var parcelUsedBonus = false
    private var parcelUsedBonusAmount = 0.0
    private var parcelOrderAmount = 0.0
    private lateinit var parcelBaggagePhotos: Map<String, ArrayList<String>>
    private var parcelHasOverheadLuggage = false
    private var parcelForAnother = false
    private var parcelPhoneNumber = ""
    private var parcelComment = ""


    @SuppressLint("SetTextI18n")
    private fun shareModel() {
        shareViewModel.selectedParcelItem.observe(viewLifecycleOwner, Observer {
            parcelStartId = it.startId
            parcelEndId = it.endId
            parcelDepDate = it.depDate
            parcelLocation = it.location
            parcelReceiverName = it.receiverName
            parcelReceiverPhoneNumber = it.receiverPhoneNumber
            parcelBaggage = it.baggage
            parcelBaggagePlaces = it.baggagePlaces
            parcelHasOverheadLuggage = it.hasOverheadLuggage
            parcelPaymentType = it.paymentType
            parcelForAnother = it.forAnother
            parcelUsedBonus = it.usedBonus
            parcelUsedBonusAmount = it.usedBonusAmount
            parcelOrderAmount = it.orderAmount
            parcelBaggagePhotos = it.baggagePhoto
            parcelPhoneNumber = it.phoneNumber
            parcelComment = it.comment


            binding.apply {
                bottomSHeet.dealMoney.text =
                    SaveData.formatPhone(parcelOrderAmount.toString()) + " " + getString(R.string.summa1)
                bottomSHeet.dealMoney1.text =
                    SaveData.formatPhone(parcelOrderAmount.toString()) + " " + getString(R.string.summa1)
                bottomSHeet.parcelType.text=parcelBaggage
                bottomSHeet.date.text=parcelDepDate
            }

            viewModel.getLocationReverse(headerMapUniversal(requireContext()), parcelLocation)

        })
    }


    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )

    }

}