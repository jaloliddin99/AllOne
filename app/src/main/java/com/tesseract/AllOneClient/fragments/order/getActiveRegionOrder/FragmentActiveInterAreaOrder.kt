package com.tesseract.AllOneClient.fragments.order.getActiveRegionOrder

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.order.DriverCarImagesAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentActiveInterareaOrderBinding
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.GetActiveOrderModelData
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentActiveInterAreaOrder : Fragment(R.layout.fragment_active_interarea_order) {
    private val args: FragmentActiveInterAreaOrderArgs by navArgs()
    private var _binding: FragmentActiveInterareaOrderBinding?=null
    private val binding get() = _binding!!
    private lateinit var driverCarImagesAdapter: DriverCarImagesAdapter
    private lateinit var viewModel: GetActiveOrderViewModel
    private lateinit var fromLatLng: String
    private lateinit var toLatLng: String

    private lateinit var getActiveOrderModelData: GetActiveOrderModelData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveInterareaOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(GetActiveOrderViewModel::class.java)
        viewModel.taxiGetActiveOrderViewModel(headerMapUniversal(requireContext()), args.id)
        clickListenersAndViewModel()

        binding.apply {
            viewReusable.cancel.setOnClickListener {
                val action=FragmentActiveInterAreaOrderDirections.actionGlobalCancelOrder(orderId, "interarea")
                findNavController().navigate(action)
            }
        }
    }

    private var orderId:Int=-1

    @SuppressLint("SetTextI18n")
    private fun clickListenersAndViewModel() {

        binding.apply {
            backToHome.setOnClickListener {
                requireActivity().onBackPressed()
            }

            signIn.setOnClickListener {
                val action = FragmentActiveInterAreaOrderDirections.actionGlobalComposeFragment()
                findNavController().navigate(action)
            }

            showFromMap.setOnClickListener {
                val action =
                    FragmentActiveInterAreaOrderDirections.actionFragmentOrderYourTaxiToFragmentRegionMapFirst(
                        fromLatLng,
                        toLatLng,
                        args.tariff,
                        args.id
                    )
                findNavController().navigate(action)
            }
            viewModel.message.observe(requireActivity(), {
                binding.loader.loader.visibility=View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })


            viewModel.getActiveOrderModelData.observe(requireActivity(), {
                binding.loader.loader.visibility=View.GONE
                getActiveOrderModelData=it
                orderId=it.id!!
                amount.text =
                    SaveData.formatPhone(it.amount!!) + " " + requireContext().getString(R.string.summa1)
                amount2.text =
                    SaveData.formatPhone(it.amount!!) + " " + requireContext().getString(R.string.summa1)
                bonusAmount.text = it.bonusAmount
                date.text = it.date
                driverAvatar.let { it1 ->
                    Glide.with(requireContext()).load(it.driverAvatar).into(
                        it1
                    )
                }
                fromLatLng = it.fromLatLng.toString()
                toLatLng = it.toLatLng.toString()

                to.text = it.to
                pickup.text = it.pickup
                title.text = it.title
                driverCar.text = it.driverCar
                paymentType.text = it.paymentType
                orderStatus.text = it.orderStatus
                driverRating.text = it.driverRating.toString()
                driverLocation.text = it.driverLocation
                driverName.text = it.driverName
                driverPhoneNumber.text = it.driverPhoneNumber
                driverPhoneNumber1=it.driverPhoneNumber!!
                driverTelegram=it.driverTelegram!!
                try {
                    if (it.placePrices!!.firstPlace.isNotEmpty()) {
                        cardUserSeats.txtFirstSeat.text = "№1 -  ${
                            it.placePrices!!.firstPlace.let { it1 ->
                                SaveData.formatPhone(
                                    it1
                                )
                            }
                        } ${requireContext().getString(R.string.summa1)}"
                        cardUserSeats.firstSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        cardUserSeats.txtFirstSeat.visibility = View.GONE
                        cardUserSeats.firstSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.veryDarkColor
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )

                    }
                } catch (e: Exception) {

                }

                try {
                    if (it.placePrices!!.secondPlace.isNotEmpty()) {
                        cardUserSeats.txtSecondSeat.text = "№2 -  ${
                            it.placePrices!!.secondPlace.let { it1 ->
                                SaveData.formatPhone(
                                    it1
                                )
                            }
                        } ${requireContext().getString(R.string.summa1)}"
                        cardUserSeats.secondSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        cardUserSeats.txtSecondSeat.visibility = View.GONE
                        cardUserSeats.secondSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.veryDarkColor
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )

                    }
                } catch (e: Exception) {

                }
                try {
                    if (it.placePrices!!.thirdPlace.isNotEmpty()) {
                        cardUserSeats.txtThirdSeat.text = "№3 -  ${
                            it.placePrices!!.thirdPlace.let { it1 ->
                                SaveData.formatPhone(
                                    it1
                                )
                            }
                        } ${requireContext().getString(R.string.summa1)}"
                        cardUserSeats.thirdSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        cardUserSeats.txtThirdSeat.visibility = View.GONE
                        cardUserSeats.thirdSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.veryDarkColor
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )

                    }
                } catch (e: Exception) {

                }

                try {
                    if (it.placePrices!!.fourthPlace.isNotEmpty()) {
                        cardUserSeats.txtFourthSeat.text = "№4 -  ${
                            it.placePrices!!.fourthPlace.let { it1 ->
                                SaveData.formatPhone(
                                    it1
                                )
                            }
                        } ${requireContext().getString(R.string.summa1)}"
                        cardUserSeats.fourthSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.green
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )
                    } else {
                        cardUserSeats.txtFourthSeat.visibility = View.GONE
                        cardUserSeats.fourthSeat.setColorFilter(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.veryDarkColor
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        )

                    }
                } catch (e: Exception) {

                }



                driverCarImagesAdapter = DriverCarImagesAdapter(it.driverCarPhotos!!)

                carImages.adapter = driverCarImagesAdapter
                carImages.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                carImages.setHasFixedSize(true)


            })

            viewReusable.telegram.setOnClickListener {
                getTelegram()
            }

            viewReusable.call.setOnClickListener {
                goToContact()
            }
        }
    }

    private var driverPhoneNumber1=""
    private var driverTelegram=""

    private fun getTelegram() {
        try {
            val telegramIntent = Intent(Intent.ACTION_VIEW)
            val telegram=if (driverTelegram.startsWith("@")){
                driverTelegram.replace("@", "")
            }else{
                driverTelegram
            }
            telegramIntent.data = Uri.parse("https://telegram.me/$telegram")
            startActivity(telegramIntent)
        } catch (e: Exception) {
            // show error message
        }
    }

    private fun goToContact() {
        try {
            val phone = driverPhoneNumber1
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        } catch (e: Exception) {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}