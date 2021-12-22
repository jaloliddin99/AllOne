package com.tesseract.AllOneClient.fragments.main.home.orderTaxi

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentOrderTaxiBinding
import com.tesseract.AllOneClient.dialogs.main.DialogExtraLargeBaggage
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime
import com.tesseract.AllOneClient.dialogs.main.DialogThreeBaggage
import com.tesseract.AllOneClient.dialogs.main.dialogChooseSeat.DialogChooseSeats
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.main.home.routeTariffs.ShareViewModel
import com.tesseract.AllOneClient.model.home.payments.ShareRegionModel
import com.tesseract.AllOneClient.model.taxiCity.Contact
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.hideKeyboard
import com.tesseract.AllOneClient.utils.onRightDrawableClicked
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentOrderTaxi : Fragment(),
    DialogChooseSeats.SelectedInfoListener,
    DialogShowTime.OnDaySelectListener,
    DialogExtraLargeBaggage.SendDataListener {


    private var _binding: FragmentOrderTaxiBinding? = null
    private val binding get() = _binding!!
    private val args: FragmentOrderTaxiArgs by navArgs()
    private var selectedPlaces = ArrayList<Int>()
    private var selectedParcelPlaceBefore = ArrayList<Int>()
    private var viewModelSeatPrices = ArrayList<String>()

    private var userNumberSelected: Boolean = false
    private var dateSelected: Boolean = false
    private var isFirstBoxChecked: Boolean = false
    private var isSecondBoxChecked: Boolean = false
    private var isThirdBoxChecked: Boolean = false
    private var isFourthBoxChecked: Boolean = false

    private var firstSeat: String = ""
    private var secondSeat: String = ""
    private var thirdSeat: String = ""
    private var fourthSeat = ArrayList<String>()
    private var selectedSeat: String = "no"
    private var depDate: String = ""
    private var tariff: String = ""

    private var startId: Int = 0
    private var endId: Int = 0
    private var arraySize: Int = 0

    private var userNumber: Int = 0

    private var amount: Float = 0f

    private var money: Float = 0F
    private var seatTotalAmount: Float = 0f

    private lateinit var viewModel: OrderTaxiViewModel
    private val shareModel: ShareViewModel by activityViewModels()
    private val shareViewModel: ShareDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderTaxiBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(this).get(OrderTaxiViewModel::class.java)
        gotoPayments()

        shareModel.selectedItem.observe(viewLifecycleOwner, { item ->
            viewModel.getRouteTariffPrices(
                headerMapUniversal(requireContext()),
                item.type!!,
                item.start,
                item.end
            )
            binding.loader.loader.visibility = View.VISIBLE

            startId = item.start.toInt()
            endId = item.end.toInt()
            tariff = item.type.toString()

            binding.apply {
                Glide.with(requireContext()).load(item.icon).into(tariffIcon)
                tariffName.text = item.name
                tariffPrice.text =
                    item.price.let { SaveData.formatPhone(it!!) } + " " + getString(R.string.summa1)
            }
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        if (args.tariff==3){
            binding.apply {
                premium.visibility=View.GONE
                haveLuggage.visibility=View.GONE
            }
        }

        chooseDialogs()
        showHideBaggage()
        hideBottom()
        switchChecker()

        restoreStateOf()
        clickListeners()
        baggageBoxes()

        viewModelListener()

        if (args.tariff != 3) {
            selectSeat()
        } else {
            fourthSeatSelected()
            selectedPlaces.clear()
            selectedPlaces.add(1)
            selectedPlaces.add(2)
            selectedPlaces.add(3)
            selectedPlaces.add(4)
            binding.selectedSeatNumbers.text = printArray(selectedPlaces)

        }


        binding.openComment.setOnClickListener {
            val action = FragmentOrderTaxiDirections.actionGlobalAddComment()
            findNavController().navigate(action)
        }

        getBackStackData<String>("commentKey", true) {
            commentText = it
            restoreStateOf()
            isReady()
        }

        getBackStackData<Contact>("selectedContact", true) {
            val phoneNum = it.numbers[0]
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
            phoneNumberOther = phoneNum
            restoreStateOf()
            isReady()
        }


        getBackStackData<String>("locationName11", true) {
            selectedLocationDisplay = it.split("###")[0]
            selectedLocation = it.split("###")[1]
            isReady()
            restoreStateOf()
        }

    }

    private fun showHideBaggage() {
        binding.apply {
            noText.setTextColor(requireContext().getColor(R.color.green))
            yesText.setTextColor(requireContext().getColor(R.color.black))
            hasBaggage.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    parcelSeat.visibility = View.VISIBLE
                    noText.setTextColor(requireContext().getColor(R.color.black))
                    yesText.setTextColor(requireContext().getColor(R.color.green))

                } else {
                    parcelSeat.visibility = View.GONE
                    noText.setTextColor(requireContext().getColor(R.color.green))
                    yesText.setTextColor(requireContext().getColor(R.color.black))
                }
            }

            findYourLocation.setOnClickListener {
                val action = FragmentOrderTaxiDirections.actionGlobalLocationReverse(false, "")
                findNavController().navigate(action)
            }
            phoneNumberForOther.onRightDrawableClicked {
                val action = FragmentOrderTaxiDirections.actionGlobalContact()
                findNavController().navigate(action)
            }
        }

    }

    private fun isReady() {
        if (userNumberSelected && dateSelected && selectedLocation.isNotEmpty()) {
            binding.goToPayment.background.setColorFilter(
                requireContext().getColor(R.color.green),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun clickListeners() {

        binding.datePicker.setOnClickListener {
            DialogShowTime(getString(R.string.departure_date), this).show(
                parentFragmentManager,
                tag
            )
        }
    }


    @SuppressLint("SetTextI18n")
    private fun restoreStateOf() {
        if (depDate.isNotEmpty()) {
            binding.date.text = depDate
        }
        if (money > 0) {
            showBottom()
            updateTotalMoney()
        }
        if (selectedPlaces.size != 0) {
            binding.selectedSeatNumbers.text = printArray(selectedPlaces)
        }
        if (binding.forYourFriend.isChecked) {
            binding.phoneNumberForOther.visibility = View.VISIBLE
            if (phoneNumberOther.isNotEmpty()) {
                binding.phoneNumberForOther.setText(phoneNumberOther)
            }
        }
        if (selectedLocationDisplay.isNotEmpty()) {
            binding.selectedLocation.text = selectedLocationDisplay
        }
        if (commentText.isNotEmpty()) {
            binding.commentText.text = commentText
        }
        colorSeats()

        if (isFirstBoxChecked) {
            binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.week_green))
            binding.boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.green)
        }
        if (isSecondBoxChecked) {
            binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.week_green))
            binding.boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.green)
        }
        if (isThirdBoxChecked) {
            binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.week_green))
            binding.boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.green)
        }

        if (isFourthBoxChecked) {
            binding.boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.week_green))
            binding.boxImagesLayout.extraLargeBox.strokeColor =
                requireContext().getColor(R.color.green)
        }
    }

    private fun viewModelListener() {
        viewModel.parcelList.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE

            if (it.size < 3) {
                binding.haveLuggage.visibility = View.GONE
                binding.parcelSeat.visibility = View.GONE
                return@observe
            }

            binding.boxImagesLayout.baggageType.text =
                if (it[0].parcel == "small") it[0].parcel else if (it[1].parcel == "small") it[1].parcel else it[2].parcel
            binding.boxImagesLayout.baggageType1.text =
                if (it[0].parcel == "medium") it[0].parcel else if (it[1].parcel == "medium") it[1].parcel else it[2].parcel
            binding.boxImagesLayout.baggageType2.text =
                if (it[0].parcel == "big") it[0].parcel else if (it[1].parcel == "big") it[1].parcel else it[2].parcel


            firstSeat =
                if (it[0].parcel == "small") it[0].price.toString() else if (it[1].parcel == "small") it[1].price.toString() else it[2].price.toString()
            secondSeat =
                if (it[0].parcel == "medium") it[0].price.toString() else if (it[1].parcel == "medium") it[1].price.toString() else it[2].price.toString()
            thirdSeat =
                if (it[0].parcel == "big") it[0].price.toString() else if (it[1].parcel == "big") it[1].price.toString() else it[2].price.toString()



            binding.boxImagesLayout.price.text =
                firstSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
            binding.boxImagesLayout.price1.text =
                secondSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
            binding.boxImagesLayout.price2.text =
                thirdSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }

        })

        viewModel.placeList.observe(requireActivity(), {
            viewModelSeatPrices.clear()
            totalMoneyPremium = 0.0
            for (i in it.indices) {
                totalMoneyPremium += it[i].price!!.toDouble()
                viewModelSeatPrices.add(it[i].price!!)
                fourthSeat.add(it[i].price!!)
            }
            if (args.tariff==3){
                if (isFirstTotal) {
                    money = totalMoneyPremium.toFloat()
                    updateTotalMoney()
                    bottomSheetPeekHeightController()
                }
                isFirstTotal = false
            }
        })
    }

    private var isFirstTotal = true
    private var totalMoneyPremium = 0.0

    private fun baggageBoxes() {


        binding.boxImagesLayout.smallBox.setOnClickListener {
            if (isFirstBoxChecked) {
                invalidateFirst()
                baggageTotalAmount=""
            } else {
                selectedSeat = binding.boxImagesLayout.baggageType.text.toString()
                money += firstSeat.toFloat()
                baggageTotalAmount=firstSeat
                binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.week_green))
                binding.boxImagesLayout.smallBox.strokeColor =
                    requireContext().getColor(R.color.green)
                isFirstBoxChecked = true

                invalidateFourth()
                invalidateSecond()
                invalidateThird()
            }

            updateTotalMoney()
            bottomSheetPeekHeightController()

        }

        binding.boxImagesLayout.middleBox.setOnClickListener {
            if (isSecondBoxChecked) {
                invalidateSecond()
                baggageTotalAmount=""
            } else {
                selectedSeat = binding.boxImagesLayout.baggageType1.text.toString()
                money += secondSeat.toFloat()
                baggageTotalAmount=secondSeat
                binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.week_green))
                binding.boxImagesLayout.middleBox.strokeColor =
                    requireContext().getColor(R.color.green)
                isSecondBoxChecked = true
            }
            invalidateFirst()
            invalidateThird()
            invalidateFourth()
            updateTotalMoney()
            bottomSheetPeekHeightController()
        }
        binding.boxImagesLayout.largeBox.setOnClickListener {
            if (isThirdBoxChecked) {
                invalidateThird()
                baggageTotalAmount=""
            } else {
                money += thirdSeat.toFloat()
                baggageTotalAmount=thirdSeat
                binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.week_green))
                selectedSeat = binding.boxImagesLayout.baggageType2.text.toString()
                binding.boxImagesLayout.largeBox.strokeColor =
                    requireContext().getColor(R.color.green)
                isThirdBoxChecked = true
            }
            invalidateFourth()
            invalidateSecond()
            invalidateFirst()

            updateTotalMoney()
            bottomSheetPeekHeightController()
        }

        binding.boxImagesLayout.extraLargeBox.setOnClickListener {

            parentFragmentManager.let {
                DialogExtraLargeBaggage(
                    getString(R.string.razmer50to50),
                    getString(R.string.massa50kg),
                    fourthSeat,
                    selectedPlaces,
                    selectedParcelPlaceBefore,
                    this
                ).show(it, tag)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun chooseDialogs() {

        binding.apply {
            chooseSeat.setOnClickListener {
                if (args.tariff != 3) {
                    if (userNumberSelected) {
                        openSelectSeatDialog()
                    }
                }
            }

            boxImagesLayout.att1.setOnClickListener {
                parentFragmentManager.let {
                    DialogThreeBaggage(
                        binding.boxImagesLayout.baggageType.text.toString(),
                        getString(R.string.razmer30to30),
                        getString(R.string.massa500),
                        binding.boxImagesLayout.price.text.toString(),
                    ).show(it, tag)
                }
            }

            boxImagesLayout.att2.setOnClickListener {
                parentFragmentManager.let {
                    DialogThreeBaggage(
                        binding.boxImagesLayout.baggageShape1.text.toString(),
                        getString(R.string.razmer40to40),
                        getString(R.string.massa5kg),
                        binding.boxImagesLayout.price1.text.toString(),
                    ).show(it, tag)
                }
            }

            boxImagesLayout.att3.setOnClickListener {
                parentFragmentManager.let {
                    DialogThreeBaggage(
                        binding.boxImagesLayout.baggageType2.text.toString(),
                        getString(R.string.razmer50to50),
                        getString(R.string.massa10Kg),
                        binding.boxImagesLayout.price2.text.toString(),
                    ).show(it, tag)
                }
            }

            boxImagesLayout.att4.setOnClickListener {
                parentFragmentManager.let {
                    DialogThreeBaggage(
                        binding.boxImagesLayout.baggageType88.text.toString(),
                        getString(R.string.razmer50to50),
                        getString(R.string.massa50kg),
                        "",
                    ).show(it, tag)
                }
            }
        }


    }

    private fun openSelectSeatDialog() {
        parentFragmentManager.let {
            DialogChooseSeats(
                userNumber,
                selectedParcelPlaceBefore,
                selectedPlaces,
                viewModelSeatPrices,
                this,
                args.tariff
            ).show(
                it,
                tag
            )
        }
    }


    private var commentText = ""
    private var selectedLocation = ""
    private var selectedLocationDisplay = ""
    private var phoneNumberOther = ""
    private var baggageTotalAmount=""

    private fun gotoPayments() {

        binding.goToPayment.setOnClickListener {
            if (!userNumberSelected || !dateSelected || selectedLocation.isEmpty()) {
                return@setOnClickListener
            }

            val hasOverheadLuggage: Boolean = binding.hasOverheadLuggage.isChecked
            val hasAirConditioner: Boolean = binding.hasAirConditioner.isChecked
            val fourYourFriend: Boolean = binding.forYourFriend.isChecked
            val phoneNumber = binding.phoneNumberForOther.text.toString().replace(" ", "")
            val selectedPlace = printArray(selectedPlaces)
            val seat = selectedSeat.lowercase()
            val parcelPlaces = printArray(selectedParcelPlaceBefore)

            val shareModel = ShareRegionModel(
                startId,
                endId,
                tariff,
                userNumber,
                selectedPlace,
                depDate,
                selectedLocation,
                seat,
                parcelPlaces,
                hasOverheadLuggage,
                hasAirConditioner,
                fourYourFriend,
                phoneNumber,
                money.toDouble(),
                seatTotalAmount.toDouble(),
                false,
                0.0,
                "cash",
                commentText,
                baggageTotalAmount,
                selectedLocationDisplay
            )
            shareViewModel.selectItem(shareModel)
            val action =
                FragmentOrderTaxiDirections.actionFragmentOrderTaxi2ToFragmentPayment(1)
            findNavController().navigate(action)
        }
    }


    override fun selectedInfo(selectedPositions: ArrayList<Int>, totalSum: Float) {
        if (amount.toInt() > 0) {
            money -= amount
        }
        amount = totalSum
        money += amount
        selectedPositions.sort()
        arraySize = selectedPositions.size
        selectedPlaces = selectedPositions

        updateTotalMoney()
        bottomSheetPeekHeightController()

        binding.selectedSeatNumbers.text = printArray(selectedPositions)
        colorSeats()

    }

    private fun colorSeats() {
        when (selectedPlaces.size) {
            0 -> {
                binding.apply {
                    userNumberSelected = false
                    selectedSeatNumbers.text = getString(R.string.no_place_selected)
                    onePerson.setBackgroundResource(R.drawable.background_grey)
                    twoPerson.setBackgroundResource(R.drawable.background_grey)
                    threePerson.setBackgroundResource(R.drawable.background_grey)
                    fourPerson.setBackgroundResource(R.drawable.background_grey)
                    onePerson.setTextColor(Color.BLACK)
                    twoPerson.setTextColor(Color.BLACK)
                    threePerson.setTextColor(Color.BLACK)
                    fourPerson.setTextColor(Color.BLACK)
                }
            }
            1 -> {
                firstSeatSelected()
            }
            2 -> {
                secondSeatSelected()
            }
            3 -> {
                thirdSeatSelected()
            }
            4 -> {
                fourthSeatSelected()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun selectDayListener(time: String) {
        binding.date.text = time
        depDate = time
        dateSelected = true
        isReady()
    }

    private fun printArray(arrayList: ArrayList<Int>): String {
        val value = StringBuilder()

        for (i in 0 until arrayList.size) {
            value.append(arrayList[i])
            value.append(",")
        }
        val printedArray: String = value.toString()
        return printedArray.dropLast(1)
    }


    private fun switchChecker() {

        binding.apply {
            phoneNumberForOther.visibility = View.GONE
            forYourFriend.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    phoneNumberForOther.visibility = View.VISIBLE
                } else {
                    phoneNumberForOther.visibility = View.GONE
                    phoneNumberForOther.setText("")
                    requireContext().hideKeyboard(requireView())

                }
            }
        }

    }

    override fun sendData(priceAmount: String, selectedPlacesList: ArrayList<Int>) {
        if (seatTotalAmount.toInt() > 0) {
            money -= seatTotalAmount
        }
        seatTotalAmount = priceAmount.toFloat()
        money += seatTotalAmount
        baggageTotalAmount= seatTotalAmount.toString()
        selectedPlacesList.sort()
        selectedParcelPlaceBefore = selectedPlacesList
        if (selectedPlacesList.size > 0) {
            binding.boxImagesLayout.extraLargeBox.strokeColor = requireContext().getColor(R.color.green)
            binding.boxImagesLayout.extraLargeBox.invalidate()
            binding.boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.week_green))
            isFourthBoxChecked = true

            invalidateFirst()
            invalidateSecond()
            invalidateThird()
        } else {
            invalidateFourth()
            baggageTotalAmount=""
        }
        updateTotalMoney()
        bottomSheetPeekHeightController()
    }

    private fun invalidateFirst() {
        binding.apply {
            if (isFirstBoxChecked) {
                binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.smallBox.strokeColor =
                    requireContext().getColor(R.color.grey)
                binding.boxImagesLayout.smallBox.invalidate()
                money -= secondSeat.toFloat()
                isFirstBoxChecked = false
            }
        }
    }

    private fun invalidateSecond() {
        binding.apply {
            if (isSecondBoxChecked) {
                binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.middleBox.strokeColor =
                    requireContext().getColor(R.color.grey)
                binding.boxImagesLayout.middleBox.invalidate()
                money -= secondSeat.toFloat()
                isSecondBoxChecked = false
            }
        }
    }

    private fun invalidateThird() {
        binding.apply {
            if (isThirdBoxChecked) {
                binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.largeBox.strokeColor =
                    requireContext().getColor(R.color.grey)
                binding.boxImagesLayout.largeBox.invalidate()
                money -= thirdSeat.toFloat()
                isThirdBoxChecked = false
            }
        }
    }

    private fun invalidateFourth() {
        binding.apply {
            if (isFourthBoxChecked) {
                binding.boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.extraLargeBox.strokeColor =
                    requireContext().getColor(R.color.grey)
                binding.boxImagesLayout.extraLargeBox.invalidate()
                money -= seatTotalAmount
                isFourthBoxChecked = false
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalMoney() {

        val totalMoney =
            "${getString(R.string.concret_summa)}: ${SaveData.formatPhone(money.toString())} ${
                getString(
                    R.string.summa1
                )
            }"
        binding.textPrice.text = totalMoney
    }

    private fun showBottom() {

        binding.line.visibility = View.VISIBLE
        binding.textPrice.visibility = View.VISIBLE
    }

    private fun bottomSheetPeekHeightController() {

        if (money.toInt() == 0) {
            hideBottom()
        } else {
            showBottom()
        }
    }

    private fun hideBottom() {
        binding.line.visibility = View.GONE
        binding.textPrice.visibility = View.GONE
    }


    private fun firstSeatSelected() {
        userNumberSelected = true
        userNumber = 1
        binding.onePerson.setBackgroundResource(R.drawable.background_green_corner_12)
        binding.twoPerson.setBackgroundResource(R.drawable.background_grey)
        binding.threePerson.setBackgroundResource(R.drawable.background_grey)
        binding.fourPerson.setBackgroundResource(R.drawable.background_grey)
        binding.onePerson.setTextColor(Color.WHITE)
        binding.twoPerson.setTextColor(Color.BLACK)
        binding.threePerson.setTextColor(Color.BLACK)
        binding.fourPerson.setTextColor(Color.BLACK)
    }

    private fun secondSeatSelected() {
        userNumberSelected = true
        userNumber = 2
        binding.onePerson.setBackgroundResource(R.drawable.background_grey)
        binding.twoPerson.setBackgroundResource(R.drawable.background_green_corner_12)
        binding.threePerson.setBackgroundResource(R.drawable.background_grey)
        binding.fourPerson.setBackgroundResource(R.drawable.background_grey)
        binding.onePerson.setTextColor(Color.BLACK)
        binding.twoPerson.setTextColor(Color.WHITE)
        binding.threePerson.setTextColor(Color.BLACK)
        binding.fourPerson.setTextColor(Color.BLACK)
    }

    private fun thirdSeatSelected() {
        userNumberSelected = true
        userNumber = 3
        binding.onePerson.setBackgroundResource(R.drawable.background_grey)
        binding.twoPerson.setBackgroundResource(R.drawable.background_grey)
        binding.threePerson.setBackgroundResource(R.drawable.background_green_corner_12)
        binding.fourPerson.setBackgroundResource(R.drawable.background_grey)
        binding.onePerson.setTextColor(Color.BLACK)
        binding.twoPerson.setTextColor(Color.BLACK)
        binding.threePerson.setTextColor(Color.WHITE)
        binding.fourPerson.setTextColor(Color.BLACK)
    }

    private fun fourthSeatSelected() {
        userNumberSelected = true
        userNumber = 4
        binding.onePerson.setBackgroundResource(R.drawable.background_grey)
        binding.twoPerson.setBackgroundResource(R.drawable.background_grey)
        binding.threePerson.setBackgroundResource(R.drawable.background_grey)
        binding.fourPerson.setBackgroundResource(R.drawable.background_green_corner_12)
        binding.onePerson.setTextColor(Color.BLACK)
        binding.twoPerson.setTextColor(Color.BLACK)
        binding.threePerson.setTextColor(Color.BLACK)
        binding.fourPerson.setTextColor(Color.WHITE)
    }

    private fun selectSeat() {
        binding.onePerson.setOnClickListener {
            firstSeatSelected()
            openSelectSeatDialog()
            isReady()
        }
        binding.twoPerson.setOnClickListener {
            secondSeatSelected()
            openSelectSeatDialog()
            isReady()
        }
        binding.threePerson.setOnClickListener {
            thirdSeatSelected()
            openSelectSeatDialog()
            isReady()
        }
        binding.fourPerson.setOnClickListener {
            if (args.tariff == 1) {
                fourthSeatSelected()
                openSelectSeatDialog()
                isReady()
            }
        }
    }

    private fun <T> Fragment.getBackStackData(
        key: String,
        singleCall: Boolean = true,
        result: (T) -> (Unit)
    ) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                result(it)
                if (singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(
                    key
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}