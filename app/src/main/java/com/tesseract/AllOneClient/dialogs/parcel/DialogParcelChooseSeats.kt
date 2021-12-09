package com.tesseract.AllOneClient.dialogs.parcel

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.DialogChooseSeatsBinding
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffPlaceListModel
import com.tesseract.AllOneClient.model.parcel.parcelSearch.Order
import com.tesseract.AllOneClient.model.parcel.parcelSearch.OtherOption

class DialogParcelChooseSeats(
    val otherOption: OtherOption,
    private val order: ArrayList<RouteTariffPlaceListModel>,
    private val listener:OnDialogCloseListener
): DialogFragment() {

    private var firstSeatSelected: Boolean = true
    private var secondSeatSelected: Boolean = true
    private var thirdSeatSelected: Boolean = true
    private var fourthSeatSelected: Boolean = true

    private var firstSeatSelectedConst:Boolean=true
    private var secondSeatSelectedConst:Boolean=true
    private var thirdSeatSelectedConst:Boolean=true
    private var fourthSeatSelectedConst:Boolean=true

    var chosenSeats = ArrayList<Int>()
    private var totalSum: Float = 0f


    private var _binding: DialogChooseSeatsBinding ?= null
    private val binding get() = _binding!!

    interface OnDialogCloseListener{
        fun selectedSeatsInformation(selectedPositions: ArrayList<Int>, totalSum: Float, userSeatIsSelected:Boolean)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= DialogChooseSeatsBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        isCancelable = false
        return binding!!.root
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(ContentValues.TAG, "onViewCreated: dialog ${otherOption.free_places}")
        if ( otherOption.free_places[0].toString()=="1"){
            firstSeatSelectedConst=false
        }
        if ( otherOption.free_places[1].toString()=="1"){
            secondSeatSelectedConst =false
        }
        if ( otherOption.free_places[2].toString()=="1"){
            thirdSeatSelectedConst=false
        }
        if ( otherOption.free_places[3].toString()=="1"){
            fourthSeatSelectedConst =false
        }


        binding.cancelImage.setOnClickListener {
            chosenSeats.clear()
            totalSum = 0f
            listener.selectedSeatsInformation(chosenSeats, totalSum, false)
            dialog?.dismiss()
        }

        binding.txtFirstSeat.text = SaveData.formatPhone(order[0].price.toString())

        binding.txtSecondSeat.text = SaveData.formatPhone(order[1].price.toString())
        binding.txtThirdSeat.text = SaveData.formatPhone(order[2].price.toString())
        binding.txtFourthSeat.text = SaveData.formatPhone(order[3].price.toString())
        binding.continueButton.setOnClickListener {
            if (chosenSeats.size>0){
                chosenSeats.sort()
                listener.selectedSeatsInformation(chosenSeats, totalSum, true)
                dialog?.dismiss()
            }
        }
        clickListeners()
        disableButtons()

    }
    private fun disableButtons(){
        if (firstSeatSelectedConst){
            firstSeatDisabled()
        }
        if (secondSeatSelectedConst){
            secondSeatDisabled()
        }
        if (thirdSeatSelectedConst){
            thirdSeatDisabled()
        }
        if (fourthSeatSelectedConst){
            fourthSeatDisabled()
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun clickListeners(){
        binding.apply {
            firstSeat.setOnClickListener {
                if (!firstSeatSelectedConst){
                    if (firstSeatSelected) {
                        firstSeatEnabled()
                    } else {
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.firstSeat.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        binding.firstSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                        binding.firstSeatText.setTextColor(Color.BLACK)
                        firstSeatSelected = true
                        binding.numOne.setTextColor(requireContext().getColor(R.color.dark_grey))
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.checkOne.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        chosenSeats.remove(1)
                        totalSum -= order[0].price?.toFloat()!!
                    }
                }
            }
            secondSeat.setOnClickListener {
                if (!secondSeatSelectedConst){
                    if (secondSeatSelected) {
                        secondSeatEnabled()
                    } else {
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.secondSeat.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        binding.secondSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                        binding.secondSeatText.setTextColor(Color.BLACK)
                        binding.numTwo.setTextColor(requireContext().getColor(R.color.dark_grey))
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.checkTwo.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        secondSeatSelected = true
                        chosenSeats.remove(2)
                        totalSum -= order[1].price?.toFloat()!!

                    }
                }
            }
            thirdSeat.setOnClickListener {
                if (!thirdSeatSelectedConst){
                    if (thirdSeatSelected) {
                        thirdSeatEnabled()
                    } else {
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.thirdSeat.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        binding.thirdSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                        binding.thirdSeatText.setTextColor(Color.BLACK)
                        binding.numThree.setTextColor(requireContext().getColor(R.color.dark_grey))
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.checkThree.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        thirdSeatSelected = true
                        chosenSeats.remove(3)
                        totalSum -= order[2].price?.toFloat()!!
                    }
                }
            }
            fourthSeat.setOnClickListener {
                if (!fourthSeatSelectedConst){
                    if (fourthSeatSelected) {
                        fourthSeatEnabled()
                    } else {
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.fourthSeat.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        binding.fourthSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image_unchecked))
                        binding.fourthSeatText.setTextColor(Color.BLACK)
                        binding.numFour.setTextColor(requireContext().getColor(R.color.dark_grey))
                        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }
                            ?.let { it2 ->
                                binding.checkFour.setColorFilter(
                                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                                )
                            }
                        fourthSeatSelected = true
                        chosenSeats.remove(4)
                        totalSum -= order[3].price?.toFloat()!!

                    }
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

    private fun firstSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red) }
            ?.let { it2 ->
                binding.firstSeat.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding.firstSeatRadio.setImageDrawable(null)
        binding.firstSeatText.setTextColor(Color.BLACK)
        firstSeatSelected = true
        binding.numOne.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding.checkOne.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun secondSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red) }
            ?.let { it2 ->
                binding.secondSeat.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding.secondSeatRadio.setImageDrawable(null)
        binding.secondSeatText.setTextColor(Color.BLACK)
        secondSeatSelected = true
        binding.numTwo.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding.checkTwo.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

    }

    private fun thirdSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red) }
            ?.let { it2 ->
                binding.thirdSeat.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding.thirdSeatRadio.setImageDrawable(null)
        binding.thirdSeatText.setTextColor(Color.BLACK)
        thirdSeatSelected = true
        binding.numThree.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding.checkThree.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun fourthSeatDisabled() {
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.red) }
            ?.let { it2 ->
                binding.fourthSeat.setColorFilter(
                    it2, android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        binding.fourthSeatRadio.setImageDrawable(null)
        binding.fourthSeatText.setTextColor(Color.BLACK)
        fourthSeatSelected = true
        binding.numFour.setTextColor(requireContext().getColor(R.color.dark_grey))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.dark_grey) }?.let { it2 ->
            binding.checkFour.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun firstSeatEnabled() {
        chosenSeats.add(1)
        totalSum += order[0].price?.toFloat()!!

        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.firstSeat.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding.firstSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding.firstSeatText.setTextColor(Color.WHITE)
        firstSeatSelected = false
        binding.numOne.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.checkOne.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun secondSeatEnabled() {
        chosenSeats.add(2)
        totalSum += order[1].price?.toFloat()!!
        Log.i("total sum ", "" + totalSum)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.secondSeat.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding.secondSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding.secondSeatText.setTextColor(Color.WHITE)
        secondSeatSelected = false
        binding.numTwo.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.checkTwo.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun thirdSeatEnabled() {
        totalSum += order[2].price?.toFloat()!!
        chosenSeats.add(3)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.thirdSeat.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding.thirdSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding.thirdSeatText.setTextColor(Color.WHITE)
        binding.numThree.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.checkThree.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        thirdSeatSelected = false
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun fourthSeatEnabled() {
        totalSum += order[3].price?.toFloat()!!
        chosenSeats.add(4)
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.fourthSeat.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        binding.fourthSeatRadio.setImageDrawable(context?.getDrawable(R.drawable.ic_radio_image))
        binding.fourthSeatText.setTextColor(Color.WHITE)
        binding.numFour.setTextColor(requireContext().getColor(R.color.green))
        context?.let { it1 -> ContextCompat.getColor(it1, R.color.green) }?.let { it2 ->
            binding.checkFour.setColorFilter(
                it2, android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
        fourthSeatSelected = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}