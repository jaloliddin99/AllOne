package com.tesseract.AllOneClient.fragments.main.home.payments

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.MyCardAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentPaymentBinding
import com.tesseract.AllOneClient.dialogs.main.DialogBonusMoney
import com.tesseract.AllOneClient.fragments.profile.addcard.getCards.GetCardViewModel
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import com.tesseract.AllOneClient.utils.dipToPixels
import com.tesseract.AllOneClient.utils.getNavOptions
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPayment : Fragment(), DialogBonusMoney.OnBonusSelected {

    private val args: FragmentPaymentArgs by navArgs()
    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel2: GetCardViewModel
    private lateinit var viewModel: PaymentsViewModel
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private lateinit var getCardData:List<GetCardData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel2 = ViewModelProvider(this).get(GetCardViewModel::class.java)
        viewModel2.getCardDataList(headerMapUniversal(requireContext()))
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel = ViewModelProvider(this).get(PaymentsViewModel::class.java)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }


        interArea()
        parcelDelivery()
        binding.btNextPayment.setOnClickListener {

            if (args.fromWhichLayout == 1) {
                interArea2()
            } else if (args.fromWhichLayout == 0) {
                parcelDelivery2()
            }
        }

        addCards()

        radioController()
        showDialog()
    }

    private fun addCards() {
        binding.apply {
            PagerUzCard.visibility = View.GONE
            tvAddCard.setOnClickListener {
                findNavController().navigate(
                    R.id.action_global_add_card_fragment,
                    null,
                    getNavOptions()
                )
            }
            addNewCard.setOnClickListener {
                findNavController().navigate(
                    R.id.action_global_add_card_fragment,
                    null,
                    getNavOptions()
                )
            }

        }

        viewModel2.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel2.cardDataList.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            binding.apply {
                if (it.isNotEmpty()) {
                    PagerUzCard.visibility = View.VISIBLE
                    tvAddCard.visibility = View.GONE
                    addNewCard.visibility = View.VISIBLE
                } else {
                    addNewCard.visibility = View.GONE
                    PagerUzCard.visibility = View.GONE
                    tvAddCard.visibility = View.VISIBLE
                }
                PagerUzCard.clipToPadding = false
                PagerUzCard.adapter = MyCardAdapter(this@FragmentPayment, it)
                getCardData=it
                PagerUzCard.setPadding(dipToPixels(requireContext(), 16f).toInt(),
                    0,
                    150,
                    0
                )
                PagerUzCard.pageMargin = dipToPixels(requireContext(), 8f).toInt()
            }
        })
    }

    private fun radioController() {
        binding.apply {
            withCardRadio.setOnClickListener {
                withCardRadio.isChecked = true
                withCashRadio.isChecked = false
                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cashCard.strokeColor = context?.getColor(R.color.grey)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cardCard.strokeColor = context?.getColor(R.color.green)!!
                cardCard.invalidate()
            }
            withCash.setOnClickListener {
                withCardRadio.isChecked = false
                withCashRadio.isChecked = true

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cashCard.strokeColor = context?.getColor(R.color.green)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cardCard.strokeColor = context?.getColor(R.color.grey)!!
                cardCard.invalidate()

            }
            withCard.setOnClickListener {
                withCardRadio.isChecked = true
                withCashRadio.isChecked = false

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cashCard.strokeColor = context?.getColor(R.color.grey)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cardCard.strokeColor = context?.getColor(R.color.green)!!
                cardCard.invalidate()
            }

            withCashRadio.setOnClickListener {

                withCardRadio.isChecked = false
                withCashRadio.isChecked = true

                withCash.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_week_green_rounded)
                cashCard.strokeColor = context?.getColor(R.color.green)!!
                cashCard.invalidate()

                withCard.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_rounded)
                cardCard.strokeColor = context?.getColor(R.color.grey)!!
                cardCard.invalidate()
            }

            withCardRadio.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isChecked) {
                    linearLayout.visibility = View.GONE
                } else {
                    linearLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showDialog() {
        SaveData.isCurrentFragment = true

        binding.bonusAmount.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (SaveData.isCurrentFragment) {
                    parentFragmentManager.let {
                        DialogBonusMoney(this, SaveData.getBalance(requireContext())!!).show(
                            it,
                            tag
                        )
                    }
                }
            }
        }
    }


    private var startId = -1
    private var endId = -1
    private var tariff = ""
    private var userNumber = -1
    private var selectedPlace = ""
    private var depDate = ""
    private var location = "0.0,0.0"
    private var seat = ""
    private var parcelPlaces = ""
    private var hasOverheadLuggage: Boolean = false
    private var hasAirConditioner: Boolean = false
    private var forAnother: Boolean = false
    private var phoneNumber: String = ""
    private var moneyTotalPrice: Double = 0.0
    private var moneyChosenSeat: Double = 0.0
    private var comment = ""
    private var baggageTotalAmount=""
    private var locationName=""

    @SuppressLint("SetTextI18n")
    private fun interArea() {

        shareViewModel.selectedItem.observe(viewLifecycleOwner, {
            startId = it.startId
            endId = it.endId
            tariff = it.tariff
            userNumber = it.userNumber
            selectedPlace = it.selectedPlaces
            depDate = it.depDate
            seat = it.seat
            parcelPlaces = it.parcelPlaces
            hasOverheadLuggage = it.hasOverheadLuggage
            hasAirConditioner = it.hasConditioner
            forAnother = it.forAnother
            phoneNumber = it.phoneNumber
            moneyTotalPrice = it.money
            moneyChosenSeat = it.selectedSeatMoney
            location = it.location
            comment = it.comment
            baggageTotalAmount=it.baggageTotalAmount
            locationName=it.locationName

            binding.apply {
                totalMoney.text =
                    SaveData.formatPhone(moneyTotalPrice.toString()) + getString(R.string.emptySpace) + getString(
                        R.string.summa1
                    )
                baggage.text =
                    SaveData.formatPhone(moneyChosenSeat.toString()) + getString(R.string.emptySpace) + getString(
                        R.string.summa1
                    )
                bonus.text = "0 ${getString(R.string.summa1)}"
                binding.totalSum.text =
                    SaveData.formatPhone(moneyTotalPrice.toString()) + getString(R.string.emptySpace) + getString(
                        R.string.summa1
                    )
            }

        })

        viewModel.newOrder.observe(requireActivity(), {

            if (SaveData.isCurrentFragment) {
                Log.i("order id ", "" + it.content.order_id)
                val action = FragmentPaymentDirections.actionFragmentPaymentToFragmentLocation(
                    args.fromWhichLayout,
                    it.content.order_id
                )
                binding.loader.loader.visibility=View.GONE
                SaveData.isCurrentFragment = false
                findNavController().navigate(action)
            }
        })
        viewModel.newOrderError.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE

        })
    }

    private fun interArea2(){
        val usedBonus: Boolean = binding.bonusAmount.isChecked
        val paymentType: String = if (binding.withCashRadio.isChecked) {
            "cash"
        } else {
            "card"
        }

        if (paymentType=="card"){
            if (getCardData.isEmpty()){
                Toast.makeText(context, getString(R.string.please_endter_card), Toast.LENGTH_SHORT).show()
                return
            }
        }

        Log.i(
            "TAG", " startId $startId, \nendId $endId, \ntariff $tariff," +
                    "\n userNumber $userNumber,\n " +
                    "selectedPlace $selectedPlace,\ndepDate $depDate \n location $location, \nlocationName $locationName \n" +
                    "seat $seat \nparcelPlaces $parcelPlaces \n hasOverheadLuggage $hasOverheadLuggage \n " +
                    "hasAirConditioner$hasAirConditioner \nforAnother $forAnother \nphoneNumber $phoneNumber,\n" +
                    "payment_type $paymentType, \n" +
                    " used_bonus $usedBonus,\n" +
                    " bonus_amount $bonusAmount\norderAmount $moneyTotalPrice" +
                    "\n baggageAmount $baggageTotalAmount \n" +
                    "comment $comment\n  cardData ${getCardData[binding.PagerUzCard.currentItem].id!!}"
        )
        binding.loader.loader.visibility=View.VISIBLE
        viewModel.interAreaNewOrder(
            headerMapUniversal(requireContext()),
            startId,
            endId,
            tariff,
            userNumber,
            selectedPlace,
            depDate,
            location,
            locationName,
            seat,
            parcelPlaces,
            if (hasOverheadLuggage) 1 else 0,
            if (hasAirConditioner) 1 else 0,
            if (forAnother) 1 else 0,
            phoneNumber,
            paymentType,
            if (usedBonus) 1 else 0,
            if (bonusAmount.isEmpty()) 0.0 else bonusAmount.toDouble(),
            moneyTotalPrice,
            if (baggageTotalAmount.isEmpty()) 0.0 else baggageTotalAmount.toDouble(),
            comment,
            getCardData[binding.PagerUzCard.currentItem].id!!
        )
        SaveData.isCurrentFragment = true
    }




    private var parcelStartId = -1
    private var parcelEndId = -1
    private var parcelDepDate = ""
    private var parcelLocation = ""
    private var parcelLocationName = ""
    private var parcelReceiverName = ""
    private var parcelReceiverPhoneNumber = ""
    private var parcelBaggage = ""
    private var parcelBaggagePlaces = ""
    private var parcelPaymentType = ""
    private var parcelUsedBonus = false
    private var parcelUsedBonusAmount = 0.0
    private var parcelOrderAmount = 0.0
    private var parcelBaggagePhotos = ArrayList<String>()
    private var parcelHasOverheadLuggage = false
    private var parcelForAnother = false
    private var parcelPhoneNumber = ""
    private var parcelComment = ""


    @SuppressLint("SetTextI18n")
    private fun parcelDelivery() {

        shareViewModel.selectedParcelItem.observe(viewLifecycleOwner, Observer {
            parcelStartId = it.startId
            parcelEndId = it.endId
            parcelDepDate = it.depDate
            parcelLocation = it.location
            parcelLocationName=it.locationName
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
                totalMoney.text =
                    SaveData.formatPhone(parcelOrderAmount.toString()) + getString(R.string.emptySpace) + getString(
                        R.string.summa1
                    )
                baggage.visibility = View.GONE

                bonus.text = "0 ${getString(R.string.summa1)}"
                binding.totalSum.text =
                    SaveData.formatPhone(parcelOrderAmount.toString()) + getString(R.string.emptySpace) + getString(
                        R.string.summa1
                    )
            }

        })

        viewModel.parcelNewOrderObserver.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            if (SaveData.isCurrentFragment) {
                val action =
                    FragmentPaymentDirections.actionFragmentPaymentToFragmentLocation(
                        args.fromWhichLayout,
                        it.content.order_id
                    )
                Log.i(TAG, "parcelDelivery: ${it.content.order_id}")
                SaveData.isCurrentFragment = false
                findNavController().navigate(action)
            }
        })

        viewModel.parcelError.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })
    }

    private fun parcelDelivery2(){

        binding.loader.loader.visibility=View.VISIBLE

        val usedBonus: Boolean = binding.bonusAmount.isChecked
        val paymentType: String = if (binding.withCashRadio.isChecked) {
            "cash"
        } else {
            "card"
        }

        Log.i(
            "TAG",
            "gotoPayments: " + "parcelStartId $parcelStartId, \nparcelEndId $parcelEndId, " +
                    "\nparcelDepDate $parcelDepDate,\n parcelLocation  $parcelLocation \n parcelLocationName $parcelLocationName" +
                    "\nparcelReceiverName $parcelReceiverName,\n parcelReceiverPhoneNumber $parcelReceiverPhoneNumber," +
                    "\n parcelBaggage $parcelBaggage,\n parcelBaggagePlaces $parcelBaggagePlaces, \npaymentType $paymentType" +
                    "\nusedBonus $usedBonus,\n bonusAmount $bonusAmount,\n " +
                    "parcelOrderAmount $parcelOrderAmount," +
                    "\n parcelHasOverheadLuggage $parcelHasOverheadLuggage,\n " +
                    "parcelForAnother $parcelForAnother,\n " +
                    "parcelPhoneNumber $parcelPhoneNumber" +
                    " \n parcelComment $parcelComment" +
                    "\nparcelBaggagePhotos $parcelBaggagePhotos"
        )

        viewModel.parcelNewOrder(
            headerMapUniversal(requireContext()),
            parcelStartId,
            parcelEndId,
            parcelDepDate,
            "12:00",
            parcelLocation,
            parcelLocationName,
            parcelReceiverName,
            parcelReceiverPhoneNumber,
            parcelBaggage,
            parcelBaggagePlaces,
            paymentType,
            if (usedBonus) 1 else 0,
            if (bonusAmount.isEmpty()) 0.0 else bonusAmount.toDouble(),
            parcelOrderAmount,
            parcelBaggagePhotos,
            if (parcelHasOverheadLuggage) 1 else 0,
            if (parcelForAnother) 1 else 0,
            parcelPhoneNumber,
            parcelComment,
            1
        )
        SaveData.isCurrentFragment = true
    }

    private var bonusAmount: String = ""

    @SuppressLint("SetTextI18n")
    override fun bonusAmount(bonus: String) {
        bonusAmount = bonus.replace(" ", "")
        if (bonusAmount.isEmpty()) {
            binding.bonusAmount.isChecked = false
        } else {
            binding.textView11.text =
                bonus + getString(R.string.emptySpace) + getString(R.string.summa1)
            binding.bonus.text = bonus + getString(R.string.emptySpace) + getString(R.string.summa1)
            binding.bonus.setTextColor(context?.getColor(R.color.black)!!)
            binding.totalSum.text =
                SaveData.formatPhone((moneyTotalPrice - bonusAmount.toFloat()).toString()) + getString(
                    R.string.emptySpace
                ) + getString(R.string.summa1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}