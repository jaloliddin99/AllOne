package com.tesseract.AllOneClient.fragments.order.getActiveParcelOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.order.DriverCarImagesAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentActiveParcelOrderBinding
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentActiveParcelOrder : Fragment() {

    private var _binding: FragmentActiveParcelOrderBinding? = null
    private val binding get() = _binding!!
    val args: FragmentActiveParcelOrderArgs by navArgs()
    private lateinit var yourTaxiAdapter: DriverCarImagesAdapter
    private lateinit var yourTaxiAdapter2: DriverCarImagesAdapter
    private lateinit var viewModel: ActiveParcelOrderViewModel
    private var phoneNumber:String=""
    private var telegram:String=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentActiveParcelOrderBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ActiveParcelOrderViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postCarPhotos.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width * 2/5
                return true
            }
        }
        binding.postAdvertisementImages.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width * 2/5
                return true
            }
        }


        viewModel.getParcelActiveOrders(headerMapUniversal(requireContext()), args.parcelId)

        binding.CRUD.cancel.setOnClickListener {
            val action=FragmentActiveParcelOrderDirections.actionGlobalCancelOrder(args.parcelId, "interarea_parcel_delivery")
            findNavController().navigate(action)
        }

        binding.CRUD.call.setOnClickListener {
            gotoContact(phoneNumber, requireContext())
        }

        binding.CRUD.telegram.setOnClickListener {
            gotoTelegram(telegram, requireContext())
        }

        binding.CRUD.chat.setOnClickListener {
            val action=FragmentActiveParcelOrderDirections.actionGlobalChat(args.parcelId)
            findNavController().navigate(action)
        }

        binding.showFromMap.setOnClickListener {
//                val action =
//                    FragmentActiveInterAreaOrderDirections.actionFragmentOrderYourTaxiToFragmentRegionMapFirst(
//                        fromLatLng,
//                        toLatLng,
//                        args.tariff,
//                        args.id
//                    )
//                findNavController().navigate(action)
            }


        viewModel.getActiveParcelData.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE

            phoneNumber=it.driverPhoneNumber!!
            telegram=it.driverTelegram!!

            binding.amount.text= SaveData.formatPhone(it.amount!!) +" "+ requireContext().getString(R.string.summa1)
            binding.amount2.text= SaveData.formatPhone(it.amount!!)+" "+ requireContext().getString(R.string.summa1)
            binding.bonusAmount.text=it.bonusAmount
            binding.date.text=it.date
            binding.driverAvatar.let { it1 ->
                Glide.with(requireContext()).load(it.driverAvatar).into(
                    it1
                )
            }

            binding.id.text="№"+it.id?.toString()
            binding.parcelType.text=it.parcelType

            if (it.hasOverHeadLuggage == true){
                binding.hasOverheadLuggage.visibility=View.VISIBLE
                binding.userSeats.visibility=View.VISIBLE

                if (!it.placePrices?.firstPlace.isNullOrEmpty()){
                    binding.cardUserSeats.txtFirstSeat.text="№1 - ${SaveData.formatPhone(it.placePrices?.firstPlace!!)} ${requireContext().getString(R.string.summa1)}"
                    binding.cardUserSeats.firstSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding.cardUserSeats.txtFirstSeat.visibility=View.GONE
                    binding.cardUserSeats.firstSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN);

                }
                if (!it.placePrices?.secondPlace.isNullOrEmpty()){
                    binding.cardUserSeats.txtSecondSeat.text="№2 - ${SaveData.formatPhone(it.placePrices?.secondPlace!!)} ${requireContext().getString(R.string.summa1)}"
                    binding.cardUserSeats.secondSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding.cardUserSeats.txtSecondSeat.visibility=View.GONE
                    binding.cardUserSeats.secondSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN)

                }

                if (!it.placePrices?.thirdPlace.isNullOrEmpty()){
                    binding.cardUserSeats.txtThirdSeat.text="№3 - ${SaveData.formatPhone(it.placePrices?.thirdPlace!!)} ${requireContext().getString(R.string.summa1)}"
                    binding.cardUserSeats.thirdSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
                }else{
                    binding.cardUserSeats.txtThirdSeat.visibility=View.GONE
                    binding.cardUserSeats.thirdSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN)

                }
                if (!it.placePrices?.fourthPlace.isNullOrEmpty()){
                    binding.cardUserSeats.txtFourthSeat.text="№4 - ${SaveData.formatPhone(it.placePrices?.fourthPlace!!)} ${requireContext().getString(R.string.summa1)}"
                    binding.cardUserSeats.fourthSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
                }else{
                    binding.cardUserSeats.txtFourthSeat.visibility=View.GONE
                    binding.cardUserSeats.fourthSeat.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN)

                }
            }else{
                binding.hasOverheadLuggage.visibility=View.GONE
                binding.userSeats.visibility=View.GONE
            }
            binding.order.text=getString(R.string.orderrr)+" №"+it.id
            binding.to.text=it.to
            binding.pickup.text=it.pickup
            binding.driverCar.text=it.driverCar
            binding.paymentType.text=it.paymentType
            binding.orderStatus.text=it.orderStatus
            binding.driverRating.text=it.driverRating.toString()
            binding.driverLocation.text=it.driverLocation
            binding.driverName.text=it.driverName
            binding.driverPhoneNumber.text=it.driverPhoneNumber
            binding.receiverName.text=it.receiver
            yourTaxiAdapter = DriverCarImagesAdapter(it.parcelPhotos!!)
            binding.postAdvertisementImages.adapter = yourTaxiAdapter
            binding.postAdvertisementImages.setHasFixedSize(true)

            yourTaxiAdapter2 = DriverCarImagesAdapter(it.driverCarPhotos!!)
            binding.postCarPhotos.adapter = yourTaxiAdapter2
            binding.postCarPhotos.setHasFixedSize(true)

        })

        binding.signIn.setOnClickListener{
            findNavController().popBackStack()
        }

        viewModel.errorM.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

    }


}