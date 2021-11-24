package com.tesseract.AllOneClient.fragments.main.home.orderTaxi

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener


@AndroidEntryPoint
class FragmentOrderTaxi : Fragment(R.layout.fragment_order_taxi),
    DialogChooseSeats.SelectedInfoListener,
    DialogShowTime.OnDaySelectListener,
    DialogExtraLargeBaggage.SendDataListener {


    private var _binding: FragmentOrderTaxiBinding?=null
    private val binding get() = _binding!!
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
    private var fourthSeat: String = ""
    private var selectedSeat: String = ""
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
        _binding= FragmentOrderTaxiBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel = ViewModelProvider(this).get(OrderTaxiViewModel::class.java)
        gotoPayments()



        shareModel.selectedItem.observe(viewLifecycleOwner, { item ->

            Log.i(TAG, "onViewCreatedadwadaw: ${item.type}")
            viewModel.getRouteTariffPrices(
                headerMapUniversal(requireContext()),
                item.type!!,
                item.start,
                item.end
            )
            println(item.type+"   "+ item.start+"jaloldo  "+item.end)
            binding.loader.loader.visibility=View.VISIBLE

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
        chooseDialogs()
        showHideBaggage()
        hideBottom()
        switchChecker()

        restoreStateOf()
        clickListeners()

        baggageBoxes()
        selectSeat()
        viewModelListener()

        binding.openComment.setOnClickListener {
            val action =
                FragmentOrderTaxiDirections.actionGlobalAddComment()
            findNavController().navigate(action)
        }

        getBackStackData<String>("commentKey", true) {
            binding.commentText.text = it
            commentText = it
            restoreStateOf()
        }


        getBackStackData<String>("locationName11", true) {
            binding.selectedLocation.text = it.split("###")[0]
            selectedLocation = it.split("###")[1]
            restoreStateOf()
        }

    }

    private fun showHideBaggage() {
        binding.apply {
            noText.setTextColor(requireContext().getColor(R.color.green))
            yesText.setTextColor(requireContext().getColor(R.color.black))
            hasBaggage.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    noText.setTextColor(requireContext().getColor(R.color.black))
                    yesText.setTextColor(requireContext().getColor(R.color.green))

                } else {
                    noText.setTextColor(requireContext().getColor(R.color.green))
                    yesText.setTextColor(requireContext().getColor(R.color.black))
                }
            }

            findYourLocation.setOnClickListener {
                val action=FragmentOrderTaxiDirections.actionGlobalLocationReverse(false, "")
                findNavController().navigate(action)
            }
            phoneNumberForOther.onRightDrawableClicked {
                val action=FragmentOrderTaxiDirections.actionGlobalContact()
                findNavController().navigate(action)
            }
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun AppCompatEditText.onRightDrawableClicked(onClicked: (view: AppCompatEditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is AppCompatEditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }


    private fun clickListeners() {

        binding.datePicker.setOnClickListener {
            DialogShowTime(getString(R.string.departure_date), this).show(
                parentFragmentManager,
                tag
            )
        }

        setEventListener(requireActivity(), object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen){
                    binding.bottomSheet.visibility = View.GONE
                }else{
                    binding.bottomSheet.visibility = View.VISIBLE
                }
            }
        })


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
        }
        colorSeats()
    }

    private fun viewModelListener() {
        viewModel.parcelList.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            binding.boxImagesLayout.baggageType.text = it[0].parcel
            binding.boxImagesLayout.baggageType1.text = it[1].parcel
            binding.boxImagesLayout.baggageType2.text = it[2].parcel
            binding.boxImagesLayout.baggageType3.text = it[3].parcel


            firstSeat = it[0].price.toString()
            secondSeat = it[1].price.toString()
            thirdSeat = it[2].price.toString()
            fourthSeat = it[3].price.toString()

            binding.boxImagesLayout.price.text =
                firstSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
            binding.boxImagesLayout.price1.text =
                secondSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
            binding.boxImagesLayout.price2.text =
                thirdSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
            binding.boxImagesLayout.price3.text =
                fourthSeat.let { it1 -> SaveData.formatPhone(it1) + " ${getString(R.string.summa1)}" }
        })

        viewModel.placeList.observe(requireActivity(), {
            viewModelSeatPrices.clear()
            for (i in it.indices) {
                it[i].price.let { it1 ->
                    if (it1 != null) {
                        viewModelSeatPrices.add(it1)
                    }
                }
            }
        })
    }

    private fun baggageBoxes() {


        binding.boxImagesLayout.smallBox.setOnClickListener {
            if (isFirstBoxChecked) {
                money -= firstSeat.toFloat()
                binding.boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.grey)
                binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.grey))
                isFirstBoxChecked = false
            } else {
                selectedSeat = binding.boxImagesLayout.baggageType.text.toString()
                money += firstSeat.toFloat()
                binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.week_green))
                binding.boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.green)
                isFirstBoxChecked = true
            }
            binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.middleBox.invalidate()
            binding.boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.largeBox.invalidate()
            if (isThirdBoxChecked) {
                money -= thirdSeat.toFloat()
            }
            if (isSecondBoxChecked) {
                money -= secondSeat.toFloat()
            }
            updateTotalMoney()
            bottomSheetPeekHeightController()
            isThirdBoxChecked = false
            isSecondBoxChecked = false
        }

        binding.boxImagesLayout.middleBox.setOnClickListener {
            if (isSecondBoxChecked) {
                money -= secondSeat.toFloat()
                binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.grey)
                isSecondBoxChecked = false
            } else {
                selectedSeat = binding.boxImagesLayout.baggageType1.text.toString()
                money += secondSeat.toFloat()
                binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.middleBox.strokeColor =
                    requireContext().getColor(R.color.green)
                isSecondBoxChecked = true
            }
            binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.smallBox.invalidate()
            binding.boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.largeBox.invalidate()
            if (isThirdBoxChecked) {
                money -= thirdSeat.toFloat()
            }
            if (isFirstBoxChecked) {
                money -= firstSeat.toFloat()
            }
            updateTotalMoney()
            bottomSheetPeekHeightController()
            isThirdBoxChecked = false
            isFirstBoxChecked = false
        }
        binding.boxImagesLayout.largeBox.setOnClickListener {
            if (isThirdBoxChecked) {

                money -= thirdSeat.toFloat()
                binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.grey))
                binding.boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.grey)
                isThirdBoxChecked = false
            } else {
                money += thirdSeat.toFloat()
                binding.boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.week_green))
                selectedSeat = binding.boxImagesLayout.baggageType2.text.toString()
                binding.boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.green)
                isThirdBoxChecked = true
            }
            binding.boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.middleBox.invalidate()
            binding.boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.smallBox.invalidate()
            if (isFirstBoxChecked) {
                money -= firstSeat.toFloat()
            }
            if (isSecondBoxChecked) {
                money -= secondSeat.toFloat()
            }
            updateTotalMoney()
            bottomSheetPeekHeightController()

            isFirstBoxChecked = false
            isSecondBoxChecked = false
        }

        binding.boxImagesLayout.extraLargeBox.setOnClickListener {

            parentFragmentManager.let {
                DialogExtraLargeBaggage(
                    "50x50 ",
                    "до 50 - kg",
                    fourthSeat,
                    selectedPlaces,
                    selectedParcelPlaceBefore,
                    this
                ).show(it, "MyCustomFragment")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun chooseDialogs() {
        chooseSeat()

        binding.boxImagesLayout.att1.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType.text.toString(),
                    "30x30 ",
                    "до 500 - Грамм",
                    binding.boxImagesLayout.price.text.toString(),
                ).show(it, tag)
            }
        }
        binding.boxImagesLayout.att2.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageShape1.text.toString(),
                    "30x30 ",
                    "до 5 - kg",
                    binding.boxImagesLayout.price1.text.toString(),
                ).show(it, tag)
            }
        }
        binding.boxImagesLayout.att3.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType2.text.toString(),
                    "30x30 ",
                    "до 10 - kg",
                    binding.boxImagesLayout.price2.text.toString(),
                ).show(it, tag)
            }
        }

        binding.boxImagesLayout.att4.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType3.text.toString(),
                    "50x50  ",
                    "до 50 - kg",
                    binding.boxImagesLayout.price3.text.toString(),
                ).show(it, tag)
            }
        }
    }

    private fun chooseSeat() {
        binding.chooseSeat.setOnClickListener {
            if (userNumberSelected) {
                openSelectSeatDialog()
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
                this
            ).show(
                it,
                tag
            )
        }
    }


    private var commentText = ""
    private var selectedLocation = ""

    private fun gotoPayments() {

        binding.goToPayment.setOnClickListener {

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
                commentText
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
        if (userNumberSelected && dateSelected) {
            binding.goToPayment.background.setColorFilter(
                requireContext().getColor(R.color.green),
                PorterDuff.Mode.MULTIPLY
            )
        }
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
        updateTotalMoney()
        selectedPlacesList.sort()
        selectedParcelPlaceBefore = selectedPlacesList
        if (selectedPlacesList.size > 0) {
            selectedSeat = binding.boxImagesLayout.baggageType3.text.toString()
            binding.boxImagesLayout.extraLargeBox.strokeColor =
                requireContext().getColor(R.color.green)
            binding.boxImagesLayout.extraLargeBox.invalidate()
            binding.boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.week_green))
            isFourthBoxChecked = true
        } else {
            binding.boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.grey))
            binding.boxImagesLayout.extraLargeBox.strokeColor =
                requireContext().getColor(R.color.grey)
            binding.boxImagesLayout.extraLargeBox.invalidate()
            isFourthBoxChecked = false
        }
        bottomSheetPeekHeightController()
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

        binding.goToPayment.background.setColorFilter(
            requireContext().getColor(R.color.green),
            PorterDuff.Mode.MULTIPLY
        )
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

        binding.goToPayment.background.setColorFilter(
            requireContext().getColor(R.color.dark_grey),
            PorterDuff.Mode.MULTIPLY
        )
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
        }
        binding.twoPerson.setOnClickListener {
            secondSeatSelected()
            openSelectSeatDialog()
        }
        binding.threePerson.setOnClickListener {
            thirdSeatSelected()
            openSelectSeatDialog()
        }
        binding.fourPerson.setOnClickListener {
            fourthSeatSelected()
            openSelectSeatDialog()
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
        _binding=null
    }


}