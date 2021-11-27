package com.tesseract.AllOneClient.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.DialogChooseSeatForPostBinding

class ExtraLargeBaggage(
    val size: String,
    val mass: String,
    val price: List<String>,
    val selectedParcelPlacesBefore: ArrayList<Int>,
    val sendDataListener: SendDataListener
) : DialogFragment() {


    private var _binding: DialogChooseSeatForPostBinding? = null
    private val binding get() = _binding!!
    private var firstSelected: Boolean = false
    private var secondSelected: Boolean = false
    private var thirdSelected: Boolean = false
    private var fourthSelected: Boolean = false
    private var priceAmount: Float = 0f
    val list = arrayListOf<Int>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.baggageShape.text = size
        binding.cancelImage.setOnClickListener {
            dialog?.dismiss()

        }


        binding.choose.setOnClickListener {
            sendDataListener.sendData(priceAmount.toString(), list)
            dialog?.dismiss()
        }

        setTextPrices(price)

        list.clear()
        priceAmount = 0f
        if (selectedParcelPlacesBefore.contains(1)) {
            enableFirst()
            firstSelected = true
            list.add(1)
            priceAmount += price[0].toFloat()
        }
        if (selectedParcelPlacesBefore.contains(2)) {
            enableSecond()
            secondSelected = true
            list.add(2)
            priceAmount += price[1].toFloat()
        }
        if (selectedParcelPlacesBefore.contains(3)) {
            enableThird()
            thirdSelected = true
            list.add(3)
            priceAmount += price[2].toFloat()
        }
        if (selectedParcelPlacesBefore.contains(4)) {
            enableFourth()
            fourthSelected = true
            list.add(4)
            priceAmount += price[3].toFloat()
        }

        seatClickListeners()


    }


    @SuppressLint("SetTextI18n")
    private fun setTextPrices(price: List<String>){
        binding.firstPrice.text=getString(R.string.sena)+" "+SaveData.formatPhone(price[0])+" "+getString(R.string.summa1)
        binding.secondPrice.text=getString(R.string.sena)+" "+SaveData.formatPhone(price[1])+" "+getString(R.string.summa1)
        binding.thirdPrice.text=getString(R.string.sena)+" "+SaveData.formatPhone(price[2])+" "+getString(R.string.summa1)
        binding.fourthPrice.text=getString(R.string.sena)+" "+SaveData.formatPhone(price[3])+" "+getString(R.string.summa1)
    }

    private fun seatClickListeners() {

        binding.firstSeat.setOnClickListener {

            if (!firstSelected) {
                enableFirst()
                list.add(1)
                priceAmount += price[0].toFloat()
                firstSelected = true
            } else {
                binding.firstSeat.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.firstRadio.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.firstItemRadio.visibility = View.VISIBLE
                binding.firstItemRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.firstRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.firstNumberColor.setTextColor(requireContext().getColor(R.color.dark_grey))
                binding.firstText.setTextColor(requireContext().getColor(R.color.black))
                priceAmount -= price[0].toFloat()
                list.remove(1)
                firstSelected = false
            }

        }

        binding.secondSeat.setOnClickListener {
            if (!secondSelected) {
                enableSecond()
                priceAmount += price[1].toFloat()
                list.add(2)
                secondSelected = true
            } else {
                binding.secondSeat.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.secondRadio.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.secondItemRadio.visibility = View.VISIBLE
                binding.secondItemRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.secondRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.secondNumberColor.setTextColor(requireContext().getColor(R.color.dark_grey))
                binding.secondText.setTextColor(requireContext().getColor(R.color.black))
                priceAmount -= price[1].toFloat()
                list.remove(2)
                secondSelected = false
            }

        }

        binding.thirdSeat.setOnClickListener {

            if (!thirdSelected) {
                enableThird()
                priceAmount += price[2].toFloat()
                list.add(3)
                thirdSelected = true
            } else {
                binding.thirdSeat.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.thirdItemRadio.visibility = View.VISIBLE
                binding.thirdItemRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.thirdRadio.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.thirdRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.thirdNumberColor.setTextColor(requireContext().getColor(R.color.dark_grey))
                binding.thirdText.setTextColor(requireContext().getColor(R.color.black))
                priceAmount -= price[2].toFloat()
                list.remove(3)
                thirdSelected = false
            }

        }

        binding.fourthSeat.setOnClickListener {

            if (!fourthSelected) {
                enableFourth()
                priceAmount += price[3].toFloat()
                list.add(4)
                fourthSelected = true
            } else {
                binding.fourthSeat.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.fourthItemRadio.visibility = View.VISIBLE
                binding.fourthItemRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.fourthRadio.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.dark_grey),
                    android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.fourthRadio.setImageResource(R.drawable.ic_radio_image_unchecked)
                binding.fourthNumberColor.setTextColor(requireContext().getColor(R.color.dark_grey))
                binding.fourthText.setTextColor(requireContext().getColor(R.color.black))
                priceAmount -= price[3].toFloat()
                list.remove(4)
                fourthSelected = false
            }

        }

    }


    private fun enableFirst() {
        binding.firstSeat.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.firstItemRadio.setImageResource(R.drawable.ic_radio_image)
        binding.firstItemRadio.visibility = View.VISIBLE
        binding.firstRadio.setImageResource(R.drawable.ic_check_box)
        binding.firstRadio.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.firstNumberColor.setTextColor(requireContext().getColor(R.color.green))
        binding.firstText.setTextColor(requireContext().getColor(R.color.white))
    }

    private fun enableSecond() {
        binding.secondSeat.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.secondItemRadio.setImageResource(R.drawable.ic_radio_image)
        binding.secondItemRadio.visibility = View.VISIBLE
        binding.secondRadio.setImageResource(R.drawable.ic_check_box)
        binding.secondRadio.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.secondNumberColor.setTextColor(requireContext().getColor(R.color.green))
        binding.secondText.setTextColor(requireContext().getColor(R.color.white))
    }

    private fun enableThird() {
        binding.thirdSeat.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.thirdItemRadio.setImageResource(R.drawable.ic_radio_image)
        binding.thirdItemRadio.visibility = View.VISIBLE
        binding.thirdRadio.setImageResource(R.drawable.ic_check_box)
        binding.thirdRadio.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.thirdNumberColor.setTextColor(requireContext().getColor(R.color.green))
        binding.thirdText.setTextColor(requireContext().getColor(R.color.white))
    }

    private fun enableFourth() {
        binding.fourthSeat.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.fourthItemRadio.setImageResource(R.drawable.ic_radio_image)
        binding.fourthItemRadio.visibility = View.VISIBLE
        binding.fourthRadio.setImageResource(R.drawable.ic_check_box)
        binding.fourthRadio.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.green),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
        binding.fourthNumberColor.setTextColor(requireContext().getColor(R.color.green))
        binding.fourthText.setTextColor(requireContext().getColor(R.color.white))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= DialogChooseSeatForPostBinding.inflate(inflater, container, false)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);

        isCancelable = false

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    interface SendDataListener {
        fun sendData(priceAmount: String, selectedPlacesList: ArrayList<Int>)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations  = R.style.DialogAnimation;
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}