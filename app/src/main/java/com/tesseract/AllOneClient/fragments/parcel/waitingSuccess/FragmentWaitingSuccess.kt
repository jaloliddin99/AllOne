package com.tesseract.AllOneClient.fragments.parcel.waitingSuccess

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentParcelWaitingSuccessBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.parcel.selectLocation.SelectLocationViewModel
import com.tesseract.AllOneClient.fragments.parcel.waiting.FragmentWaitingOrderAcceptArgs
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentWaitingSuccess: Fragment() {
    private val args:FragmentWaitingSuccessArgs by navArgs()
    private lateinit var viewModel: SelectLocationViewModel
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private lateinit var binding:FragmentParcelWaitingSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentParcelWaitingSuccessBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            bottomSHeet.orderId.text = args.parcelOrderId.toString()
            bottomSHeet.orderId2.text = "№" + args.parcelOrderId.toString()


            viewModel.data.observe(viewLifecycleOwner, Observer {
                bottomSHeet.location.text=it.region
            })
        }
        shareModel()


        binding.run {

            driver.setOnClickListener {
                val action=FragmentWaitingSuccessDirections.actionGlobalParcelActiveOrder(args.parcelOrderId)
                findNavController().navigate(action)
            }

            goToMain.setOnClickListener{
                val directions = FragmentWaitingSuccessDirections.actionGlobalComposeFragment()
                findNavController().navigate(directions)
            }

        }

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
    private lateinit var parcelBaggagePhotos: Map<String, String>
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


}