package com.tesseract.AllOneClient.fragments.order.getActiveParcelOrder

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
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
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentActiveParcelOrder : Fragment(R.layout.fragment_active_parcel_order) {

    private var binding: FragmentActiveParcelOrderBinding? = null
    val args: FragmentActiveParcelOrderArgs by navArgs()
    lateinit var dialog: Dialog
    private lateinit var yourTaxiAdapter: DriverCarImagesAdapter
    private lateinit var yourTaxiAdapter2: DriverCarImagesAdapter
    private lateinit var viewModel: ActiveParcelOrderViewModel

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentOrderPostsBinding = FragmentActiveParcelOrderBinding.bind(view)
        viewModel = ViewModelProvider(this).get(ActiveParcelOrderViewModel::class.java)
        binding = fragmentOrderPostsBinding

        binding?.postCarPhotos?.layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width * 2/5
                return true
            }
        }
        binding?.postAdvertisementImages?.layoutManager = object : LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width * 2/5
                return true
            }
        }

        loader()

        viewModel.getParcelActiveOrders(headerMapUniversal(requireContext()), args.parcelId)
        viewModel.getActiveParcelData.observe(requireActivity(), Observer {
            dialog.dismiss()
            binding?.amount?.text= SaveData.formatPhone(it.amount!!) +" "+ context?.getString(R.string.summa1)
            binding?.amount2?.text= it.amount?.let { it1 -> SaveData.formatPhone(it1) }+" "+ context?.getString(R.string.summa1)
            binding?.bonusAmount?.text=it.bonusAmount
            binding?.date?.text=it.date
            binding?.driverAvatar?.let { it1 ->
                Glide.with(requireContext()).load(it.driverAvatar).into(
                    it1
                )
            }
            if (it.hasOverHeadLuggage == true){
                binding?.hasOverheadLuggage?.visibility=View.VISIBLE
                binding?.userSeats?.visibility=View.VISIBLE

                if (!it.placePrices?.firstPlace.isNullOrEmpty()){
                    binding?.cardUserSeats?.txtFirstSeat?.text="№1 -  ${it.placePrices?.firstPlace?.let { it1 ->
                        SaveData.formatPhone(
                            it1
                        )
                    }} ${context?.getString(R.string.summa1)}"
                    binding?.cardUserSeats?.firstSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding?.cardUserSeats?.txtFirstSeat?.visibility=View.GONE
                    binding?.cardUserSeats?.firstSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN);

                }
                if (!it.placePrices?.secondPlace.isNullOrEmpty()){
                    binding?.cardUserSeats?.txtSecondSeat?.text="№2 -  ${it.placePrices?.secondPlace?.let { it1 ->
                        SaveData.formatPhone(
                            it1
                        )
                    }} ${context?.getString(R.string.summa1)}"
                    binding?.cardUserSeats?.secondSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding?.cardUserSeats?.txtSecondSeat?.visibility=View.GONE
                    binding?.cardUserSeats?.secondSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN);

                }
                if (!it.placePrices?.thirdPlace.isNullOrEmpty()){
                    binding?.cardUserSeats?.txtThirdSeat?.text="№3 -  ${it.placePrices?.thirdPlace?.let { it1 ->
                        SaveData.formatPhone(
                            it1
                        )
                    }} ${context?.getString(R.string.summa1)}"
                    binding?.cardUserSeats?.thirdSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding?.cardUserSeats?.txtThirdSeat?.visibility=View.GONE
                    binding?.cardUserSeats?.thirdSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN);

                }
                if (!it.placePrices?.fourthPlace.isNullOrEmpty()){
                    binding?.cardUserSeats?.txtFourthSeat?.text="№4 -  ${it.placePrices?.fourthPlace?.let { it1 ->
                        SaveData.formatPhone(
                            it1
                        )
                    }} ${context?.getString(R.string.summa1)}"
                    binding?.cardUserSeats?.fourthSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                }else{
                    binding?.cardUserSeats?.txtFourthSeat?.visibility=View.GONE
                    binding?.cardUserSeats?.fourthSeat?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.veryDarkColor), android.graphics.PorterDuff.Mode.SRC_IN);

                }
            }else{
                binding?.hasOverheadLuggage?.visibility=View.GONE
                binding?.userSeats?.visibility=View.GONE
            }
            binding?.to?.text=it.to
            binding?.pickup?.text=it.pickup
            binding?.driverCar?.text=it.driverCar
            binding?.paymentType?.text=it.paymentType
            binding?.orderStatus?.text=it.orderStatus
            binding?.driverRating?.text=it.driverRating?.toString()
            binding?.driverLocation?.text=it.driverLocation
            binding?.driverName?.text=it.driverName
            binding?.driverPhoneNumber?.text=it.driverPhoneNumber
            binding?.receiverName?.text=it.receiver


            yourTaxiAdapter = DriverCarImagesAdapter(it.parcelPhotos!!)
            binding!!.postAdvertisementImages.adapter = yourTaxiAdapter
            binding!!.postAdvertisementImages.setHasFixedSize(true)


            yourTaxiAdapter2 = DriverCarImagesAdapter(it.driverCarPhotos!!)
            binding!!.postCarPhotos.adapter = yourTaxiAdapter2
            binding!!.postCarPhotos.setHasFixedSize(true)


        })

        viewModel.errorM.observe(requireActivity(), Observer {
            dialog.dismiss()
            Toast.makeText(context, getString(R.string.smth_went_wrong), Toast.LENGTH_SHORT).show()
        })

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.showFromMap?.setOnClickListener {

        }
    }

    private fun loader(){
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


}