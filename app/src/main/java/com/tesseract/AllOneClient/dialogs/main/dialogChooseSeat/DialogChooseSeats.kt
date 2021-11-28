package com.tesseract.AllOneClient.dialogs.main.dialogChooseSeat

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.DialogChooseSeatsBinding
import com.tesseract.AllOneClient.fragments.main.home.orderTaxi.OrderTaxiViewModel
import com.tesseract.AllOneClient.utils.changeMoneyType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogChooseSeats(
    private val userNumber: Int,
    private val selectedParcelPlacesBefore: ArrayList<Int>,
    private val arrayList: ArrayList<Int>,
    private val viewModelSeatPrices: ArrayList<String>,
    private val listener: SelectedInfoListener,
    private val tariff: Int
) :
    DialogFragment(R.layout.dialog_choose_seats) {
    private var firstSeatSelected: Boolean = true
    private var secondSeatSelected: Boolean = true
    private var thirdSeatSelected: Boolean = true
    private var fourthSeatSelected: Boolean = true
    private var isButtonEnabled: Boolean = false
    var chosenSeats = ArrayList<Int>()
    private var totalSum: Float = 0f

    private var binding: DialogChooseSeatsBinding? = null

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val chooseSeatsBinding = DialogChooseSeatsBinding.bind(view)
        binding = chooseSeatsBinding


        binding?.cancelImage?.setOnClickListener {
            chosenSeats.clear()
            totalSum = 0f
            restorePreviousState()
            listener.selectedInfo(chosenSeats, totalSum)
            dialog?.dismiss()
        }

        binding?.txtFirstSeat?.text = SaveData.formatPhone(viewModelSeatPrices[0])
        binding?.txtSecondSeat?.text = SaveData.formatPhone(viewModelSeatPrices[1])
        binding?.txtThirdSeat?.text = SaveData.formatPhone(viewModelSeatPrices[2])
        binding?.txtFourthSeat?.text = SaveData.formatPhone(viewModelSeatPrices[3])
        binding?.continueButton?.setOnClickListener {
            listener.selectedInfo(chosenSeats, totalSum)
            dialog?.dismiss()
        }


        restoreState()

        clickListeners()

    }

    private fun restorePreviousState() {

        if (arrayList.contains(1)) {
            firstSeatEnabled()
        }
        if (arrayList.contains(2)) {
            secondSeatEnabled()
        }
        if (arrayList.contains(3)) {
            thirdSeatEnabled()
        }
        if (arrayList.contains(4)) {
            fourthSeatEnabled()
        }

        if (selectedParcelPlacesBefore.contains(1)) {
            firstSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(2)) {
            secondSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(3)) {
            thirdSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(4)) {
            fourthSeatDisabled()
        }
    }

    private fun restoreState() {
        if (userNumber == 4) {
            isButtonEnabled = true
            if (!selectedParcelPlacesBefore.contains(1)) {
                firstSeatEnabled()
            }
            if (!selectedParcelPlacesBefore.contains(2)) {
                secondSeatEnabled()
            }
            if (!selectedParcelPlacesBefore.contains(3)) {
                thirdSeatEnabled()
            }
            if (!selectedParcelPlacesBefore.contains(4)) {
                fourthSeatEnabled()
            }
        } else {
            if (arrayList.contains(1)) {
                firstSeatEnabled()
            }
            if (arrayList.contains(2)) {
                secondSeatEnabled()
            }
            if (arrayList.contains(3)) {
                thirdSeatEnabled()
            }
            if (arrayList.contains(4)) {
                fourthSeatEnabled()
            }
        }

        if (selectedParcelPlacesBefore.contains(1)) {
            firstSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(2)) {
            secondSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(3)) {
            thirdSeatDisabled()
        }
        if (selectedParcelPlacesBefore.contains(4)) {
            fourthSeatDisabled()
        }


    }

    private fun firstSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red_seat_color) }
            ?.let { it2 ->
                binding?.firstSeat?.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding?.firstSeatRadio?.setImageDrawable(null)
        binding?.firstSeatText?.setTextColor(Color.BLACK)
        firstSeatSelected = true
        binding?.numOne?.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding?.checkOne?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun secondSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red_seat_color) }
            ?.let { it2 ->
                binding?.secondSeat?.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding?.secondSeatRadio?.setImageDrawable(null)
        binding?.secondSeatText?.setTextColor(Color.BLACK)
        secondSeatSelected = true
        binding?.numTwo?.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding?.checkTwo?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

    }

    private fun thirdSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red_seat_color) }
            ?.let { it2 ->
                binding?.thirdSeat?.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding?.thirdSeatRadio?.setImageDrawable(null)
        binding?.thirdSeatText?.setTextColor(Color.BLACK)
        thirdSeatSelected = true
        binding?.numThree?.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding?.checkThree?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun fourthSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red_seat_color) }
            ?.let { it2 ->
                binding?.fourthSeat?.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding?.fourthSeatRadio?.setImageDrawable(null)
        binding?.fourthSeatText?.setTextColor(Color.BLACK)
        fourthSeatSelected = true
        binding?.numFour?.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding?.checkFour?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

    }

    private fun firstSeatEnabled() {
        chosenSeats.add(1)
        totalSum += viewModelSeatPrices[0].toFloat()

        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.firstSeat?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding?.firstSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding?.firstSeatText?.setTextColor(Color.WHITE)
        firstSeatSelected = false
        binding?.numOne?.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.checkOne?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun secondSeatEnabled() {
        chosenSeats.add(2)
        totalSum += viewModelSeatPrices[1].toFloat()
        Log.i("total sum ", "" + totalSum)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.secondSeat?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding?.secondSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding?.secondSeatText?.setTextColor(Color.WHITE)
        secondSeatSelected = false
        binding?.numTwo?.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.checkTwo?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun thirdSeatEnabled() {
        totalSum += viewModelSeatPrices[2].toFloat()
        chosenSeats.add(3)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.thirdSeat?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding?.thirdSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding?.thirdSeatText?.setTextColor(Color.WHITE)
        binding?.numThree?.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.checkThree?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        thirdSeatSelected = false
    }

    private fun fourthSeatEnabled() {
        totalSum += viewModelSeatPrices[3].toFloat()
        chosenSeats.add(4)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.fourthSeat?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding?.fourthSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding?.fourthSeatText?.setTextColor(Color.WHITE)
        binding?.numFour?.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding?.checkFour?.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        fourthSeatSelected = false
    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    private fun clickListeners() {
        binding?.firstSeat?.setOnClickListener {
            if (tariff == 1) {
                if (!selectedParcelPlacesBefore.contains(1)) {
                    if (firstSeatSelected) {
                        firstSeatEnabled()
                    } else {
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding?.firstSeat?.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        binding?.firstSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                        binding?.firstSeatText?.setTextColor(Color.BLACK)
                        firstSeatSelected = true
                        binding?.numOne?.setTextColor(requireContext().getColor(R.color.dark_grey))
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding?.checkOne?.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        chosenSeats.remove(1)
                        totalSum -= viewModelSeatPrices[0].toFloat()
                    }
                }
            }
        }

        binding?.secondSeat?.setOnClickListener {
            if (!selectedParcelPlacesBefore.contains(2)) {
                if (secondSeatSelected) {
                    secondSeatEnabled()
                } else {
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.secondSeat?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    binding?.secondSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                    binding?.secondSeatText?.setTextColor(Color.BLACK)
                    binding?.numTwo?.setTextColor(requireContext().getColor(R.color.dark_grey))
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.checkTwo?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    secondSeatSelected = true
                    chosenSeats.remove(2)
                    totalSum -= viewModelSeatPrices[1].toFloat()

                }
            }
        }
        binding?.thirdSeat?.setOnClickListener {
            if (!selectedParcelPlacesBefore.contains(3)) {
                if (thirdSeatSelected) {
                    thirdSeatEnabled()
                } else {
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.thirdSeat?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    binding?.thirdSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                    binding?.thirdSeatText?.setTextColor(Color.BLACK)
                    binding?.numThree?.setTextColor(requireContext().getColor(R.color.dark_grey))
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.checkThree?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    thirdSeatSelected = true
                    chosenSeats.remove(3)
                    totalSum -= viewModelSeatPrices[2].toFloat()
                }
            }
        }
        binding?.fourthSeat?.setOnClickListener {

            if (!selectedParcelPlacesBefore.contains(4)) {
                if (fourthSeatSelected) {

                    fourthSeatEnabled()
                } else {
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.fourthSeat?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    binding?.fourthSeatRadio?.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                    binding?.fourthSeatText?.setTextColor(Color.BLACK)
                    binding?.numFour?.setTextColor(requireContext().getColor(R.color.dark_grey))
                    context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                        ?.let { it2 ->
                            binding?.checkFour?.setColorFilter(
                                it2, android.graphics.PorterDuff.Mode.SRC_IN
                            )
                        }
                    fourthSeatSelected = true
                    chosenSeats.remove(4)
                    totalSum -= viewModelSeatPrices[3].toFloat()

                }
            }


        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_choose_seats, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        isCancelable = false
        return view
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface SelectedInfoListener {
        fun selectedInfo(selectedPositions: ArrayList<Int>, totalSum: Float)
    }
}