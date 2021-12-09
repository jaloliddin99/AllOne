package com.tesseract.AllOneClient.fragments.parcel.parcelMain

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.AddBaggageImagesAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentPostServiceSelectionBinding
import com.tesseract.AllOneClient.dialogs.ExtraLargeBaggage
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime
import com.tesseract.AllOneClient.dialogs.main.DialogTariffNotFound
import com.tesseract.AllOneClient.dialogs.main.DialogThreeBaggage
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.model.home.payments.ShareParcelModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class FragmentParcelMain : Fragment(),
    DialogShowTime.OnDaySelectListener,
    ExtraLargeBaggage.SendDataListener,
    AddBaggageImagesAdapter.OnImageClickListener {

    private var _binding: FragmentPostServiceSelectionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PostServiceSelectionViewModel

    var viewModelSeatPrices = ArrayList<String>()
    var selectedParcelPlaceBegore = ArrayList<Int>()

    private var isFirstBoxChecked: Boolean = false
    private var isSecondBoxChecked: Boolean = false
    private var isThirdBoxChecked: Boolean = false
    private var isFourthBoxChecked: Boolean = false
    private var money: Float = 0f


    private lateinit var addBaggageImagesAdapter: AddBaggageImagesAdapter
    private var addBaggageImageModel = ArrayList<Any>()

    private var firstSeat: String = ""
    private var secondSeat: String = ""
    private var thirdSeat: String = ""
    private var selectedSeat: String = ""

    private val shareViewModel: ShareDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostServiceSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostServiceSelectionViewModel::class.java)

        binding.loader.loader.visibility = View.GONE
        viewModel.parcelList.observe(requireActivity(), {

            if (it.size < 3) {
                DialogTariffNotFound("${Common.startRegion} ${Common.startDistrict}", "${Common.endRegion} ${Common.endDistrict}")
                    .show(parentFragmentManager, tag)
                binding.bottomSheet.visibility=View.GONE
                binding.loader.loader.visibility=View.GONE
                binding.linearLayout.visibility = View.GONE
                return@observe
            }

            binding.bottomSheet.visibility=View.VISIBLE

            firstSeat =
                if (it[0].parcel == "small") it[0].price.toString() else if (it[1].parcel == "small") it[1].price.toString() else it[2].price.toString()
            secondSeat =
                if (it[0].parcel == "medium") it[0].price.toString() else if (it[1].parcel == "medium") it[1].price.toString() else it[2].price.toString()
            thirdSeat =
                if (it[0].parcel == "big") it[0].price.toString() else if (it[1].parcel == "big") it[1].price.toString() else it[2].price.toString()
            binding.apply {
                relativeView.visibility = View.GONE
                loader.loader.visibility = View.GONE

                boxImagesLayout.price.text =
                    SaveData.formatPhone(firstSeat) + " ${getString(R.string.summa1)}"
                boxImagesLayout.price1.text =
                    SaveData.formatPhone(secondSeat) + " ${getString(R.string.summa1)}"
                boxImagesLayout.price2.text =
                    SaveData.formatPhone(thirdSeat) + " ${getString(R.string.summa1)}"

            }


        })

        viewModel.parcelError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.placeList.observe(requireActivity(), {
            viewModelSeatPrices.clear()
            for (i in it.indices) {
                viewModelSeatPrices.add(it[i].price!!)
            }
        })

        addBaggageImageModel.add(R.drawable.rectangle_baggage_image)
        addBaggageImagesAdapter = AddBaggageImagesAdapter(addBaggageImageModel, this)
        binding.baggageImageRecycler.adapter = addBaggageImagesAdapter
        binding.baggageImageRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.baggageImageRecycler.setHasFixedSize(false)

        binding.datePicker.setOnClickListener {
            DialogShowTime(getString(R.string.departure_date), this).show(
                parentFragmentManager,
                tag
            )
        }

        binding.backToHome.setOnClickListener {
            val action =
                FragmentParcelMainDirections.actionGlobalComposeFragment()
            findNavController().navigate(action)
        }

        binding.findYourLocation.setOnClickListener {
            val action =
                FragmentParcelMainDirections.actionGlobalLocationReverse(false, "")
            findNavController().navigate(action)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        FragmentParcelMainDirections.actionGlobalComposeFragment()
                    findNavController().navigate(action)
                }
            }
            )

        responsibleForLocationSelection()
        layoutBaggage()
        layoutDialogs()
        showHideEdittext()
        newParcelOrder()
        comments()
    }

    private fun comments() {
        getBackStackData<String>("commentKey", true) {
            binding.commentText.text = it
            commentText = it
            restoreState()
        }

        getBackStackData<String>("locationName11", true) {
            selectedLocationDisplay=it.split("###")[0]
            binding.selectedLocation.text = selectedLocationDisplay
            selectedLocation = it.split("###")[1]
            makeButtonGreen()
            restoreState()
        }

        binding.commentText.setOnClickListener {
            findNavController().navigate(FragmentParcelMainDirections.actionGlobalAddComment())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun responsibleForLocationSelection() {
        if (Common.destination == 10) {
            if (Common.startRegionId.isNotEmpty() && Common.startDistrictId.isNotEmpty()) {
                binding.startDestination.text = "${Common.startRegion} ${Common.startDistrict}"
                binding.startDestinationChange.text = getString(R.string.change)
            }
        }

        if (Common.destination == 11) {
            if (Common.endRegionId.isNotEmpty() && Common.endDistrictId.isNotEmpty()) {
                binding.endDestination.text = "${Common.endRegion} ${Common.endDistrict}"
                binding.endDestinationTextChange.text = getString(R.string.change)
            }
        }

        if (Common.startRegionId.isNotEmpty() && Common.endRegionId.isNotEmpty()
            && Common.startDistrictId.isNotEmpty() && Common.endDistrictId.isNotEmpty()
        ) {
            binding.loader.loader.visibility = View.VISIBLE
            viewModel.getRouteTariffPrices(
                headerMapUniversal(requireContext()),
                "standard",
                Common.startDistrictId,
                Common.endDistrictId
            )

            binding.apply {
                startDestinationChange.text = getString(R.string.change)
                endDestinationTextChange.text = getString(R.string.change)

                startDestination.text = Common.startRegion + " " + Common.startDistrict
                endDestination.text = Common.endRegion + " " + Common.endDistrict

            }
        }


        binding.startDestinationChange.setOnClickListener {
            Common.destination = 10
            val action =
                FragmentParcelMainDirections.actionFragmentPostServiceSelectionToFragmentRegions()
            findNavController().navigate(action)
        }

        binding.endDestinationTextChange.setOnClickListener {
            Common.destination = 11
            val action =
                FragmentParcelMainDirections.actionFragmentPostServiceSelectionToFragmentRegions()
            findNavController().navigate(action)
        }
    }

    private fun showHideEdittext() {
        binding.apply {
            phoneNumberForOther.visibility = View.GONE
            forYourFriend.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    phoneNumberForOther.visibility = View.VISIBLE
                } else {
                    phoneNumberForOther.visibility = View.GONE
                    phoneNumberForOther.visibility = View.GONE
                    phoneNumberForOther.setText("")
                    requireContext().hideKeyboard(requireView())
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun layoutDialogs() {

        binding.boxImagesLayout.att1.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType.text.toString(),
                    getString(R.string.razmer30to30),
                    getString(R.string.massa500),
                    binding.boxImagesLayout.price.text.toString(),
                ).show(it, tag)
            }
        }
        binding.boxImagesLayout.att2.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageShape1.text.toString(),
                    getString(R.string.razmer40to40),
                    getString(R.string.massa5kg),
                    binding.boxImagesLayout.price1.text.toString(),
                ).show(it, tag)
            }
        }
        binding.boxImagesLayout.att3.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType2.text.toString(),
                    getString(R.string.razmer50to50),
                    getString(R.string.massa10Kg),
                    binding.boxImagesLayout.price2.text.toString(),
                ).show(it, tag)
            }
        }
        binding.boxImagesLayout.att4.setOnClickListener {
            parentFragmentManager.let {
                DialogThreeBaggage(
                    binding.boxImagesLayout.baggageType3.text.toString(),
                    getString(R.string.razmer50to50),
                    getString(R.string.massa50kg),
                    binding.boxImagesLayout.price3.text.toString(),
                ).show(it, tag)
            }
        }

    }

    private fun layoutBaggage() {

        binding.apply {
            boxImagesLayout.smallBox.setOnClickListener {
                if (isFirstBoxChecked) {
                    invalidateFirst()
                    updateTotalMoney()

                } else {
                    boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.green)
                    boxImagesLayout.smallBox.invalidate()
                    boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.week_green))
                    selectedSeat = "small"
                    boxImagesLayout.firstRadio.isChecked = true
                    money += firstSeat.toFloat()
                    isFirstBoxChecked = true
                    invalidateSecond()
                    invalidateFourth()
                    invalidateThird()
                    updateTotalMoney()
                }

                makeButtonGreen()

            }
            binding.boxImagesLayout.middleBox.setOnClickListener {
                if (isSecondBoxChecked) {
                    invalidateSecond()
                    updateTotalMoney()
                } else {
                    selectedSeat = "medium"
                    boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.green)
                    boxImagesLayout.middleBox.invalidate()
                    boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.week_green))
                    boxImagesLayout.secondRadio.isChecked = true
                    money += secondSeat.toFloat()
                    isSecondBoxChecked = true
                    invalidateThird()
                    invalidateFourth()
                    invalidateFirst()
                    updateTotalMoney()
                }
                makeButtonGreen()
            }
            binding.boxImagesLayout.largeBox.setOnClickListener {
                if (isThirdBoxChecked) {
                    invalidateThird()
                    updateTotalMoney()
                } else {
                    selectedSeat = "big"
                    boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.green)
                    boxImagesLayout.largeBox.invalidate()
                    boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.week_green))
                    boxImagesLayout.thirdRadio.isChecked = true
                    money += thirdSeat.toFloat()
                    isThirdBoxChecked = true
                    invalidateFirst()
                    invalidateSecond()
                    invalidateFourth()
                    updateTotalMoney()
                }
                makeButtonGreen()
            }
            boxImagesLayout.extraLargeBox.setOnClickListener {

                ExtraLargeBaggage(
                    getString(R.string.razmer50to50),
                    getString(R.string.massa50kg),
                    viewModelSeatPrices,
                    selectedParcelPlaceBegore,
                    this@FragmentParcelMain
                ).show(parentFragmentManager, tag)
            }
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

    private var commentText = ""
    private var selectedLocation = ""
    private var selectedLocationDisplay = ""

    private fun newParcelOrder() {
        binding.goToPayment.setOnClickListener {

            if (selectedLocation.isEmpty()||selectedDate.isEmpty()||selectedSeat.isEmpty()){
                return@setOnClickListener
            }

            if (selectedLocationDisplay.isEmpty()){
                return@setOnClickListener
            }

            val receiverName = binding.receiverName.text.toString()
            val receiverPhone = binding.phoneNumber.text.toString()

            if (receiverName.isEmpty()){
                binding.receiverName.error = getString(R.string.please_enter_receiver_name)
                return@setOnClickListener
            }
            if (receiverPhone.isEmpty()){
                binding.phoneNumber.error = getString(R.string.please_enter_receiver_number)
                return@setOnClickListener
            }



            val start = Common.startDistrictId
            val end = Common.endDistrictId
            val depDate = selectedDate

            val hasOverheadLuggage: Boolean = binding.hasOverheadLuggage.isChecked
            val fourYourFriend: Boolean = binding.forYourFriend.isChecked
            val phoneNumber = binding.phoneNumberForOther.text.toString().replace(" ", "")
            val baggage = selectedSeat
            val baggagePlaces = printArray(selectedParcelPlaceBegore)
            val orderAmount = money.toString().replace(" ", "")


            val shareParcelModel = ShareParcelModel(
                start.toInt(),
                end.toInt(),
                depDate,
                selectedLocation,
                selectedLocationDisplay,
                receiverName,
                receiverPhone,
                baggage,
                baggagePlaces,
                "cash",
                false,
                0.0,
                orderAmount.toDouble(),
                imageBase64,
                hasOverheadLuggage,
                fourYourFriend,
                phoneNumber,
                commentText
            )

            shareViewModel.selectedParcelItem(shareParcelModel)
            val action = FragmentParcelMainDirections.actionFragmentPostServiceSelectionToFragmentPayment(0)

            findNavController().navigate(action)
        }
    }

    private var imageBase64 = ArrayList<String>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {

                val selectedFile: Uri? = data!!.data
                if (data.clipData == null) {
                    addBaggageImageModel.add(data.data.toString())
                    binding.baggageImageRecycler.adapter = addBaggageImagesAdapter
                    binding.baggageImageRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.baggageImageRecycler.setHasFixedSize(false)
                }

                if (selectedFile != null) {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedFile
                        )
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    val byteArray: ByteArray = outputStream.toByteArray()
                    val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    imageBase64.add(encodedString)

                }

            }
        }
    }


    private var selectedDate: String = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun selectDayListener(time: String) {
        binding.date.text = time
        selectedDate = time
        makeButtonGreen()
    }

    private fun makeButtonGreen(){
        if (selectedLocation.isNotEmpty() &&
            selectedDate.isNotEmpty() && selectedSeat.isNotEmpty()){
            binding.goToPayment.background.setColorFilter(
                requireContext().getColor(R.color.green),
                PorterDuff.Mode.MULTIPLY
            )
        }else{
            binding.goToPayment.background.setColorFilter(
                requireContext().getColor(R.color.dark_grey),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }
    private fun restoreState() {
        binding.apply {
            if (selectedDate.isNotEmpty()) {
                date.text = selectedDate
            }
            if (this@FragmentParcelMain.selectedLocationDisplay.isNotEmpty()){
                selectedLocation.text=selectedLocationDisplay
            }
            if (money > 0) {
                updateTotalMoney()
            }
            if (forYourFriend.isChecked) {
                phoneNumberForOther.visibility = View.VISIBLE
            }
            if (addBaggageImageModel.size > 1) {
                addBaggageImageModel.removeAt(addBaggageImageModel.size - 1)
            }
            if (isFirstBoxChecked) {
                boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.green)
                boxImagesLayout.smallBox.invalidate()
                boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.week_green))
            }
            if (isSecondBoxChecked) {
                boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.green)
                boxImagesLayout.middleBox.invalidate()
                boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.week_green))
            }
            if (isThirdBoxChecked) {
                boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.green)
                boxImagesLayout.largeBox.invalidate()
                boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.week_green))
            }
            if (isFourthBoxChecked) {
                boxImagesLayout.extraLargeBox.strokeColor = requireContext().getColor(R.color.green)
                boxImagesLayout.extraLargeBox.invalidate()
                boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.week_green))
            }
        }

    }


    private var seatTotalAmount: Float = 0f
    override fun sendData(priceAmount: String, selectedPlacesList: ArrayList<Int>) {
        binding.apply {
            if (seatTotalAmount.toInt() > 0) {
                money -= seatTotalAmount
            }
            seatTotalAmount = priceAmount.toFloat()
            money += seatTotalAmount
            updateTotalMoney()
            selectedPlacesList.sort()
            selectedParcelPlaceBegore = selectedPlacesList

            if (selectedPlacesList.size > 0) {
                boxImagesLayout.fourthRadio.isChecked = true
                boxImagesLayout.extraLargeBox.strokeColor = requireContext().getColor(R.color.green)
                boxImagesLayout.extraLargeBox.invalidate()
                boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.week_green))
                isFourthBoxChecked = true
                selectedSeat = "seat"
                invalidateFirst()
                invalidateSecond()
                invalidateThird()
                updateTotalMoney()
            } else {
                boxImagesLayout.extraLargeBox.strokeColor = requireContext().getColor(R.color.grey)
                boxImagesLayout.extraLargeBox.invalidate()
                boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.grey))
                boxImagesLayout.fourthRadio.isChecked = false
                updateTotalMoney()
                isFourthBoxChecked = false
            }

            makeButtonGreen()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateTotalMoney() {
        val totalMoney =
            "${getString(R.string.concret_summa)}: ${SaveData.formatPhone(money.toString())} ${
                getString(R.string.summa1)
            }"
        binding.textPrice.text = totalMoney
    }

    private var PICK_IMAGE_INTENT = 1

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_INTENT)

    }

    private fun invalidateFirst() {
        binding.apply {
            if (isFirstBoxChecked) {
                isFirstBoxChecked = false
                boxImagesLayout.smallBox.strokeColor = requireContext().getColor(R.color.grey)
                boxImagesLayout.smallBox.invalidate()
                boxImagesLayout.constraint1.setBackgroundColor(requireContext().getColor(R.color.grey))
                boxImagesLayout.firstRadio.isChecked = false
                money -= firstSeat.toFloat()
            }
        }
    }

    private fun invalidateSecond() {
        binding.apply {
            if (isSecondBoxChecked) {
                isSecondBoxChecked = false
                boxImagesLayout.middleBox.strokeColor = requireContext().getColor(R.color.grey)
                boxImagesLayout.middleBox.invalidate()
                boxImagesLayout.constraint2.setBackgroundColor(requireContext().getColor(R.color.grey))
                boxImagesLayout.secondRadio.isChecked = false
                money -= secondSeat.toFloat()
            }
        }
    }

    private fun invalidateThird() {
        binding.apply {
            if (isThirdBoxChecked) {
                isThirdBoxChecked = false
                boxImagesLayout.largeBox.strokeColor = requireContext().getColor(R.color.grey)
                boxImagesLayout.largeBox.invalidate()
                boxImagesLayout.constraint3.setBackgroundColor(requireContext().getColor(R.color.grey))
                boxImagesLayout.thirdRadio.isChecked = false
                money -= thirdSeat.toFloat()
            }
        }
    }

    private fun invalidateFourth() {
        binding.apply {
            if (isFourthBoxChecked) {
                isFourthBoxChecked = false
                boxImagesLayout.extraLargeBox.strokeColor = requireContext().getColor(R.color.grey)
                boxImagesLayout.extraLargeBox.invalidate()
                boxImagesLayout.constraint4.setBackgroundColor(requireContext().getColor(R.color.grey))
                boxImagesLayout.fourthRadio.isChecked = false
                money -= seatTotalAmount
            }
        }
    }

    override fun onAddClick(position: Int) {
        openGallery()
    }

    override fun onDeleteClick(position: Int) {
        addBaggageImageModel.removeAt(position)
        addBaggageImagesAdapter.notifyItemRemoved(position)
    }

    private fun <T> Fragment.getBackStackData(
        key: String,
        singleCall: Boolean = true,
        result: (T) -> (Unit)
    ) {
        findNavController().currentBackStackEntry!!.savedStateHandle.getLiveData<T>(key)
            .observe(viewLifecycleOwner) {
                result(it)
                if (singleCall) findNavController().currentBackStackEntry!!.savedStateHandle.remove<T>(
                    key
                )
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}