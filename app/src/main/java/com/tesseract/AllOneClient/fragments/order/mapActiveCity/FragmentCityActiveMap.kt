package com.tesseract.AllOneClient.fragments.order.mapActiveCity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentCityActiveMapBinding
import com.tesseract.AllOneClient.dialogs.city.CityOrderFinished
import com.tesseract.AllOneClient.dialogs.commonDialogs.driverRating.DialogDriverRating
import com.tesseract.AllOneClient.fragments.order.mapActiveRegion.MapActivityRegionViewModel
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tarif.view.*

@AndroidEntryPoint
class FragmentCityActiveMap : Fragment(), CityOrderFinished.OnLickListener, DialogDriverRating.GoToHomeListener {
    private lateinit var routingViewModel: MapActivityRegionViewModel
    private var _binding: FragmentCityActiveMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CityActiveViewModel
    private var driverPhoneNumber=""
    private lateinit var mMap:GoogleMap
    private var driverTelegram=""
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private val callback = OnMapReadyCallback { googleMap ->

        mMap=googleMap

        googleMap.uiSettings.isCompassEnabled=false

        val sydney = LatLng(41.0, 69.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18f))


        binding.topLinear.call.setOnClickListener {
            gotoContact(driverPhoneNumber, requireContext())
        }
        binding.bottomLinear.call.setOnClickListener {
            gotoContact(driverPhoneNumber, requireContext())
        }

        var isGone=true
        binding.topLinear.write.setOnClickListener {
            if (isGone){
                binding.chatAndTelegram.visibility=View.VISIBLE
                isGone=false
            }else{
                binding.chatAndTelegram.visibility=View.GONE
                isGone=true
            }
        }

        binding.telegram.setOnClickListener {
            gotoTelegram(driverTelegram, requireContext())
        }

        binding.chat.setOnClickListener {
            val action=FragmentCityActiveMapDirections.actionGlobalChat(args.orderId)
            findNavController().navigate(action)
        }
        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.cityActiveOrderModel(headerMapUniversal(requireContext()), args.orderId)
        viewModel.cityActiveOrderModel.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            driverPhoneNumber=it.driver_phone_number
            driverTelegram=it.driver_telegram
            binding.tariff.text=it.tariff
            if (it.comments.isNotEmpty()){
                binding.comments.text=it.comments
            }else{
                binding.commentLine.visibility=View.GONE
                binding.commentLinear.visibility=View.GONE
            }
            if (!it.has_conditioner){
                binding.hasConditioner2.visibility=View.GONE
            }
            binding.eta.text=it.eta.toString()
            binding.driverCar.text=it.driver_car
            binding.driverName.text=it.driver_name
            binding.driverRating.text=it.driver_rating.toString()
            binding.paymentType.text=it.payment_type
            binding.amount.text=SaveData.formatPhone(it.amount)+" "+getString(R.string.summa1)

            if (it.for_another_phone_number.isNotEmpty()){
                binding.forAnotherPhoneNumber.text=it.for_another_phone_number
            }else{
                binding.forAnotherPhoneNumber2.visibility=View.GONE
                binding.forAnotherPhoneNumber3.visibility=View.GONE
            }
            binding.orderStatus.text=it.order_status

        })

        val list = ArrayList<Double>()
        list.add(69.24469210862974)
        list.add(41.28742448540935)

        val listEnd = ArrayList<Double>()
        listEnd.add(68.66563218764084)
        listEnd.add(40.83162302490042)

        val listBig = ArrayList<ArrayList<Double>>()
        listBig.add(list)
        listBig.add(listEnd)

        val routingModel = RoutindDetails(false, listBig)
        routingViewModel.mapGetRouting(headerMapUniversal(requireContext()), routingModel)

        routingViewModel.locationRouting.observe(viewLifecycleOwner, {
            decodeToLatLng(it)
        })
    }

    val args:FragmentCityActiveMapArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityActiveMapBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CityActiveViewModel::class.java)
        routingViewModel=ViewModelProvider(this).get(MapActivityRegionViewModel::class.java)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            false
        )
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val bottomSheet: View = view.findViewById(R.id.bottomSheetNestedScrollView)


        binding.paymentMethod.setOnClickListener {
            val action = FragmentCityActiveMapDirections.actionMapsFragmentToFragmentCityPaymentMethod()
            findNavController().navigate(action)
        }

        binding.topLinear.cancel.setOnClickListener {
            val action=FragmentCityActiveMapDirections.actionGlobalCancelOrder(args.orderId, "city")
            findNavController().navigate(action)
        }
        binding.bottomLinear.cancel.setOnClickListener {
            val action=FragmentCityActiveMapDirections.actionGlobalCancelOrder(args.orderId, "city")
            findNavController().navigate(action)
        }

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {



            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.topLinearLayout.alpha = 1f

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.topLinearLayout.alpha = 0f
                        binding.chattingOptionUI.alpha = 0f

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding.chattingOptionUI.alpha = 0f
                    }

                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

                binding.topLinearLayout.alpha = 1f - slideOffset
                binding.bottomSheetBackgroundLayer.alpha = slideOffset

                if (slideOffset < 0.5f) {
                    binding.chattingOptionUI.alpha = 2f * (0.5f - slideOffset)
                }
            }
        })

        val height: Float? = binding.chattingOptionUI.height.toFloat()
        var isAnimated: Boolean = false

        binding.chattingOptionUI.translationY = -height!!

        binding.topLinear.write.setOnClickListener {
            if (!isAnimated) {
                binding.chattingOptionUI.animate().translationY(height).duration = 500
                isAnimated = true
            } else {
                binding.chattingOptionUI.animate().translationY(-height).duration = 500
                isAnimated = false
            }
        }
    }

    override fun rate() {
        val menuFragment = DialogDriverRating("city", args.orderId, 1,this)
        menuFragment.isCancelable = false
        menuFragment.show(parentFragmentManager, menuFragment.tag)
    }

    override fun aboutTrip() {

    }

    private fun decodeToLatLng(list: List<List<Double>>) {
        val latLngList=ArrayList<LatLng>()
        for (i in list.indices) {

            if (i!=list.lastIndex) {
                val lng: Double = list[i][0]
                val lat: Double = list[i][1]
                latLngList.add(LatLng(lat, lng))
            }

        }
        mMap.addPolyline(
            PolylineOptions().addAll(
                latLngList
            ).width(12F).color(Color.parseColor("#02C65C")).geodesic(true).jointType(JointType.ROUND)
        )
    }




    override fun gotoHome() {
        val action = FragmentCityActiveMapDirections.actionGlobalComposeFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}