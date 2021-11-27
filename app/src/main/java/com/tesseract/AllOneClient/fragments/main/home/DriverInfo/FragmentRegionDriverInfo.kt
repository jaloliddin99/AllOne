package com.tesseract.AllOneClient.fragments.main.home.DriverInfo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.RegionDriverInfoAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentRegionDriwerInfoBinding
import com.tesseract.AllOneClient.dialogs.main.otherOptionDialogs.DialogChooseSeats
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.model.home.SearchModel.OtherOption
import com.tesseract.AllOneClient.utils.dipToPixels
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentRegionDriverInfo: Fragment(R.layout.fragment_region_driwer_info)
    , RegionDriverInfoAdapter.OnItemClickListener, OnMapReadyCallback, DialogChooseSeats.OnDialogCloseListener {
    val args:FragmentRegionDriverInfoArgs by navArgs()
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private lateinit var mMap: GoogleMap
    private var isGPS = false
    private val value: Float = 14F
    private var userSeatIsSelected:Boolean=false

    private lateinit var viewModel: UpdateNewOrderViewModel
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private  var _binding: FragmentRegionDriwerInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var regionDriverInfoAdapter: RegionDriverInfoAdapter

    override fun onDetach() {
        super.onDetach()
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            true
        )
    }

    override fun onResume() {
        super.onResume()
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding= FragmentRegionDriwerInfoBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(UpdateNewOrderViewModel::class.java)



        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.updateNewOrder.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.interAreaError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.interAreaBooking.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            val action=FragmentRegionDriverInfoDirections.actionFragmentRegionDriverInfoToFragmentRegionTaxiConfirmation(tariff, orderId, driverId)
            findNavController().navigate(action)
        })


        bottomSheetStateChangeListener()
        requestPermission()
        imageTextSetter()
        booking()
    }

    private fun booking(){
        binding.apply {
            selectPlaceCardView.setOnClickListener {
                otherOption=content.other_options[args.selectedPosition]
                DialogChooseSeats(
                    otherOption,
                    content.order,
                    this@FragmentRegionDriverInfo
                ).show(
                    parentFragmentManager,
                    tag
                )
            }

            bookNow.setOnClickListener {
                if (!args.isYourRequest){
                    if (!userSeatIsSelected){
                        shadowBackground.visibility=View.VISIBLE
                        taxiDriverLocationPresenter.translationZ=0f
                        selectPlaceText.setTextColor(context?.getColor(R.color.red)!!)
                    }else{
                        if (driverId!=-11 && orderId!=-11){
                            loader.loader.visibility=View.VISIBLE
                            viewModel.interAreaBooking(headerMapUniversal(requireContext()), orderId, driverId)
                        }
                    }
                }else{
                    if (driverId!=-11 && orderId!=-11){
                        loader.loader.visibility=View.VISIBLE
                        viewModel.interAreaBooking(headerMapUniversal(requireContext()), orderId, driverId)
                    }

                }

            }
        }
    }
    private var tariff:String=""
    private var orderId:Int=-11
    private var driverId:Int=-11
    private lateinit var otherOption: OtherOption
    private var location=""
    private lateinit var content:Content
    @SuppressLint("SetTextI18n")
    private fun imageTextSetter(){
        shareViewModel.mutableSearchItem.observe(viewLifecycleOwner,  {
            content=it
            orderId=it.order.id.toInt()
            tariff=it.order.tariff


            binding.apply {
                if (args.isYourRequest){
                    yourRequest(it)
                    driverId=it.your_request[args.selectedPosition].id

                    placeNotAccording.visibility=View.GONE
                    selectPlaceCardView.visibility=View.GONE
                    materialCardView.visibility=View.VISIBLE
                    tariff.text=it.order.tariff
                    places.text=it.order.places
                    price.text= SaveData.formatPhone(it.order.price)+context?.getString(R.string.summa1)
                    driverLocation.text=it.your_request[args.selectedPosition].driver_location
                    baggage.text=it.your_request[args.selectedPosition].baggage
                    driverRating.text=it.your_request[args.selectedPosition].driver_rating.toString()
                    location=it.your_request[args.selectedPosition].driver_last_location
                }else{
                    otherOptions(it)
                    driverId=it.your_request[args.selectedPosition].id
                    location=it.your_request[args.selectedPosition].driver_last_location
                    selectPlaceCardView.visibility=View.VISIBLE
                    placeNotAccording.visibility=View.VISIBLE
                    materialCardView.visibility=View.GONE
                    tariff.text=it.order.tariff
                    places.text=it.order.places
                    price.text= SaveData.formatPhone(it.order.price)+context?.getString(R.string.summa1)
                    driverLocation.text=it.other_options[args.selectedPosition].driver_location
                    driverRating.text=it.other_options[args.selectedPosition].driver_rating.toString()
                }
                regionDriverInfoAdapter= RegionDriverInfoAdapter(it.your_request[args.selectedPosition].driver_car_photos, this@FragmentRegionDriverInfo)
                recyclerCarImages.adapter=regionDriverInfoAdapter
                recyclerCarImages.setHasFixedSize(true)

                recyclerCarImages.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                        lp.width = width * 2/5
                        return true
                    }
                }
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled=false
        mMap.uiSettings.isCompassEnabled = false
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val myLocation = locationManager!!.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (myLocation != null) {
            val yourLocation = LatLng(myLocation.latitude, myLocation.longitude)
            mMap.addMarker(MarkerOptions().position(yourLocation).icon(context?.bitmapDescriptorFromVector(R.drawable.ic_my_location_on_map)))
            val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
            mMap.animateCamera(update)

        }


        val driverLocation = LatLng(location.split(",")[0].toDouble(), location.split(",")[1].toDouble())
        mMap.addMarker(MarkerOptions().position(driverLocation).icon(context?.bitmapDescriptorFromVector(R.drawable.ic_car_top_30_degree)))
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(driverLocation, value)
        mMap.animateCamera(update)
        binding.requestFocus.setOnClickListener {
            mMap.animateCamera(update)
        }

        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPS) {
            showSettingsAlert()
        }
    }

    private fun requestPermission() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
                )
            }
        } else {
            locationListener?.let {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, it)
            }
        }
    }

    private fun bottomSheetStateChangeListener(){
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) }
        } else {
            locationListener?.let {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f,
                    it
                )
            }
        }

        val bottomSheet: View = requireView().findViewById(R.id.bottomSheetNestedScrollView)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

                val params = CoordinatorLayout.LayoutParams(
                    CoordinatorLayout.LayoutParams.MATCH_PARENT,
                    CoordinatorLayout.LayoutParams.MATCH_PARENT
                )
                val bottom=dipToPixels(requireContext(), 350f+slideOffset*(200f))
                params.setMargins(0, 0, 0, bottom.toInt())
                binding.apply {
                    relativeLayout.layoutParams = params
                    taxiDriverLocationPresenter.alpha=1-slideOffset
                    taxiDriverLocationPresenter.translationZ= dipToPixels(requireContext(), 8f*(1-slideOffset))
                }

            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (!isGPS) {
                        showSettingsAlert()
                    }
                }
                return
            }
        }
    }


    private fun yourRequest(it: Content){
        binding.apply {
            var counter=0
            for (element in it.your_request[args.selectedPosition].free_places){
                if (element.toString()=="1"){
                    counter++
                }
            }
            freePlaces.text=counter.toString()

            if (it.your_request[args.selectedPosition].free_places[0].toString()=="0"){
                rec1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (it.your_request[args.selectedPosition].free_places[1].toString()=="0"){
                rec4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            if (it.your_request[args.selectedPosition].free_places[2].toString()=="0"){
                rec3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (it.your_request[args.selectedPosition].free_places[3].toString()=="0"){
                rec2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }
    private fun otherOptions(it: Content){
        binding.apply {

            var counter=0
            for (element in it.other_options[args.selectedPosition].free_places){
                if (element.toString()=="1"){
                    counter++
                }
            }
            freePlaces.text=counter.toString()

            if (it.other_options[args.selectedPosition].free_places[0].toString()=="0"){
                rec1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec1.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            if (it.other_options[args.selectedPosition].free_places[1].toString()=="0"){
                rec4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec4.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }

            if (it.other_options[args.selectedPosition].free_places[2].toString()=="0"){
                rec3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec3.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }
            if (it.other_options[args.selectedPosition].free_places[3].toString()=="0"){
                rec2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN)
            }else{
                rec2.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }
    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("GPS is not Enabled!")
        alertDialog.setMessage("Do you want to turn on GPS?")
        alertDialog.setPositiveButton("Yes") { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        alertDialog.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }
    override fun onItemClick(position: Int) {

    }
    private fun Context.bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        vectorDrawable.draw(Canvas(bitmap))
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    @SuppressLint("SetTextI18n")
    override fun selectedSeatsInformation(
        selectedPositions: ArrayList<Int>,
        totalSum: Float,
        userSeatIsSelected: Boolean
    ) {
        binding.apply {
            if (userSeatIsSelected){
                this@FragmentRegionDriverInfo.userSeatIsSelected =userSeatIsSelected
                shadowBackground.visibility=View.GONE
                placeNotAccording.visibility=View.GONE
                selectPlaceCardView.visibility=View.GONE
                materialCardView.visibility=View.VISIBLE
                taxiDriverLocationPresenter.translationZ= dipToPixels(requireContext(), 8f)
                selectPlaceText.setTextColor(context?.getColor(R.color.black)!!)
                price.text=SaveData.formatPhone(totalSum.toString())+getString(R.string.summa1)
                var seats=""
                var orderedSeats=""
                for (i in selectedPositions.indices){
                    orderedSeats+=if (i==selectedPositions.lastIndex){
                        "${selectedPositions[i]}"
                    }else{
                        "${selectedPositions[i]},"
                    }
                    seats += if (i==selectedPositions.lastIndex){
                        "#${selectedPositions[i]}"
                    }else{
                        "#${selectedPositions[i]}, "
                    }
                }

                places.text=seats
                loader.loader.visibility=View.VISIBLE
                viewModel.update(headerMapUniversal(requireContext()), orderId, selectedPositions.size, orderedSeats)

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}