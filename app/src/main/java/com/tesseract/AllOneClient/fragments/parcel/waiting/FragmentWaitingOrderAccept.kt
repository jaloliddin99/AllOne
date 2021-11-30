package com.tesseract.AllOneClient.fragments.parcel.waiting

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
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
import com.tesseract.AllOneClient.dialogs.sockets.DialogOrderCancelled
import com.tesseract.AllOneClient.fragments.main.home.FragmentRegionTaxiConfirmationDirections
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.parcel.selectLocation.SelectLocationViewModel
import com.tesseract.AllOneClient.services.SocketHandler
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class FragmentWaitingOrderAccept : Fragment(), DialogOrderCancelled.OnLickListener {


    private lateinit var viewModel:SelectLocationViewModel
    val args: FragmentWaitingOrderAcceptArgs by navArgs()
    private var _binding: FragmentParcelWaitingOrderAcceptBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private val time= 10*60*1000L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParcelWaitingOrderAcceptBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            true
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val timer = object: CountDownTimer(time, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    val progress=(1.0-millisUntilFinished.toDouble()/time.toDouble())*100

                    Log.i("TAG", "onTick: $progress")

                    wrongProgress.progress= progress.toFloat()
                }

                override fun onFinish() {
                    wrongProgress.progress=100f
                }
            }
            timer.start()
        }

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()

        mSocket.on("client_order_${args.parcelOrderId}") { args ->
            if (args[0] != null) {
                val response = args[0] as JSONObject
                activity?.runOnUiThread {
                    val direction=response.getString("status")
                    if (direction=="cancelled"){
                        DialogOrderCancelled(this).show(parentFragmentManager, tag)
                    }
                    if (direction=="accepted"){

                    }
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
            )



        binding.cancel.setOnClickListener {
            val action=
                FragmentRegionTaxiConfirmationDirections.actionGlobalCancelOrder(args.parcelOrderId, "interarea_parcel_delivery")
            findNavController().navigate(action)
        }


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
    private var parcelLocationDisplay = ""
    private var parcelReceiverName = ""
    private var parcelReceiverPhoneNumber = ""
    private var parcelBaggage = ""
    private var parcelBaggagePlaces = ""
    private var parcelPaymentType = ""
    private var parcelUsedBonus = false
    private var parcelUsedBonusAmount = 0.0
    private var parcelOrderAmount = 0.0
    private var parcelBaggagePhotos=ArrayList<String>()
    private var parcelHasOverheadLuggage = false
    private var parcelForAnother = false
    private var parcelPhoneNumber = ""
    private var parcelComment = ""


    @SuppressLint("SetTextI18n")
    private fun shareModel() {
        shareViewModel.selectedParcelItem.observe(viewLifecycleOwner, {

            parcelStartId = it.startId
            parcelEndId = it.endId
            parcelDepDate = it.depDate
            parcelLocation = it.location
            parcelLocationDisplay = it.locationName
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun orderAgain() {

    }

}