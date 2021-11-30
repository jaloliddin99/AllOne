package com.tesseract.AllOneClient.fragments.order.mapActiveRegion

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentRegionMapFirstBinding
import com.tesseract.AllOneClient.dialogs.commonDialogs.driverRating.DialogDriverRating
import com.tesseract.AllOneClient.fragments.order.getActiveRegionOrder.GetActiveOrderViewModel
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.PlacePrices
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentRegionActiveMap : Fragment(), OnMapReadyCallback, DialogDriverRating.GoToHomeListener {
    val args: FragmentRegionActiveMapArgs by navArgs()
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap
    private var binding: FragmentRegionMapFirstBinding? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetBehavior2: BottomSheetBehavior<*>? = null
    private var driverLastLocation=""
    private var pickupLatLng=""
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private lateinit var viewModel: GetActiveOrderViewModel
    private var isGPS = false
    private val value: Float = 16f
    private var driverPhoneNumber=""
    private var driverTelegram=""


    private lateinit var routingViewModel: MapActivityRegionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegionMapFirstBinding.inflate(inflater, container, false)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
        routingViewModel=ViewModelProvider(this).get(MapActivityRegionViewModel::class.java)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        viewModel = ViewModelProvider(this).get(GetActiveOrderViewModel::class.java)

        viewModel.taxiGetActiveOrderViewModel(headerMapUniversal(requireContext()), args.id)



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

        routingViewModel.locationRouting.observe(viewLifecycleOwner, Observer {
            decodeToLatLng(it)
        })
        clickListeners()
        requestPermission()
    }

    private var driverId:Int=0
    @SuppressLint("SetTextI18n")
    private fun setUI(){

        viewModel.getActiveOrderModelData.observe(viewLifecycleOwner, {
            binding?.amount2?.text=SaveData.formatPhone(it.amount!!)+" "+getString(R.string.summa1)
            binding?.view1?.driverCar?.text=it.driverCar
            binding?.view1?.tariff?.text=it.tariff
            binding?.view1?.amount?.text=SaveData.formatPhone(it.amount!!)+" "+getString(R.string.summa1)
            binding?.orderStatus?.text=it.orderStatus
            binding?.pickup?.text=it.pickup
//            binding?.seekbarReusable?.time?.text=it.time
//            binding?.seekbarReusable?.distance?.text=it.distance
            driverLastLocation=it.driver_last_location!!
            pickupLatLng=it.pickUpLtLng!!
            driverPhoneNumber=it.driverPhoneNumber!!
            driverTelegram=it.driverTelegram!!
            driverId= it.driverId?.toInt()!!

            setSelectedPrices(it.placePrices!!)
            val pickup = LatLng(pickupLatLng.split(",")[0].toDouble(), pickupLatLng.split(",")[1].toDouble())
            mMap.addMarker(MarkerOptions().position(pickup).icon(context?.bitmapDescriptorFromVector(R.drawable.ic_my_location_on_map)))
            val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(pickup, value)
            mMap.animateCamera(update)

            val driverLocation = LatLng(driverLastLocation.split(",")[0].toDouble(), driverLastLocation.split(",")[1].toDouble())
            mMap.addMarker(MarkerOptions().position(driverLocation).icon(context?.bitmapDescriptorFromVector(R.drawable.ic_car_top_30_degree)))
            val updateCam: CameraUpdate = CameraUpdateFactory.newLatLngZoom(driverLocation, value)
            mMap.animateCamera(updateCam)
        })
    }

    private fun setSelectedPrices(placePrices: PlacePrices){
        var place=""
        try {
            if (placePrices.firstPlace.isNotEmpty()) {
                place += "№1 - ${SaveData.formatPhone(placePrices.firstPlace)} ${getString(R.string.summa1)}"
            }
        } catch (e: Exception) {

        }

        try {
            if (placePrices.secondPlace.isNotEmpty()) {
                place += ", №2 - ${SaveData.formatPhone(placePrices.secondPlace)} ${getString(R.string.summa1)}"
            }
        } catch (e: Exception) {

        }
        try {
           if (placePrices.thirdPlace.isNotEmpty()) {
                place += ", №3 - ${SaveData.formatPhone(placePrices.thirdPlace)} ${getString(R.string.summa1)}"
            }
        } catch (e: Exception) {

        }

        try {
            if (placePrices.fourthPlace.isNotEmpty()) {
                place += ", №4 - ${SaveData.formatPhone(placePrices.fourthPlace)} ${getString(R.string.summa1)}"
            }
        } catch (e: Exception) {

        }
        binding?.placePrices?.text=place
    }


    private fun startEndLatLng(): ArrayList<ArrayList<Double>> {
        val arrayList = ArrayList<ArrayList<Double>>()
        val list = ArrayList<Double>()

        list.add(args.fromLatLng.split(",")[1].toDouble())
        list.add(args.fromLatLng.split(",")[0].toDouble())
        arrayList.add(list)
        list.clear()
        list.add(args.toLatLng.split(",")[1].toDouble())
        list.add(args.toLatLng.split(",")[0].toDouble())
        arrayList.add(list)
        return arrayList
    }

    private fun requestPermission() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
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

    private fun clickListeners() {

        val bottomSheet: View = requireView().findViewById(R.id.bottomSheetNestedScrollView)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

                binding?.topView1?.alpha=1-slideOffset
            }
        })

        val bottomSheet2: View = requireView().findViewById(R.id.onArrive)
        mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2)
        (mBottomSheetBehavior2 as BottomSheetBehavior<*>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
            }
        })

        BottomSheetBehavior.from(bottomSheet2).state = BottomSheetBehavior.STATE_EXPANDED

        var isVisible = false
//        binding?.seekbarReusable?.cardView?.setOnClickListener {
//            if (!isVisible) {
//                bottomSheet2.visibility = View.VISIBLE
//                bottomSheet.visibility = View.GONE
//                isVisible = true
//            } else {
//                bottomSheet2.visibility = View.GONE
//                bottomSheet.visibility = View.VISIBLE
//                isVisible = false
//            }
//        }

        binding?.bookNow?.setOnClickListener {
            if (isVisible) {
                val menuFragment = DialogDriverRating(args.type, args.id, driverId, this)
                menuFragment.isCancelable = true
                menuFragment.show(parentFragmentManager, menuFragment.tag)
            }
        }


        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.apply {
            topView.cancel.setOnClickListener {
                val action=FragmentRegionActiveMapDirections.actionGlobalCancelOrder(args.id, args.type)
                findNavController().navigate(action)
            }
            bottomView.cancel.setOnClickListener {
                val action=FragmentRegionActiveMapDirections.actionGlobalCancelOrder(args.id, args.type)
                findNavController().navigate(action)
            }
            topView.call.setOnClickListener {
                goToContact()
            }
            bottomView.call.setOnClickListener {
                goToContact()
            }
            topView.telegram.setOnClickListener {
                getTelegram()
            }
            bottomView.telegram.setOnClickListener {
                getTelegram()
            }
            call.setOnClickListener {
                goToContact()
            }
            telegram.setOnClickListener {
                getTelegram()
            }
        }


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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (!isGPS) {
                        showSettingsAlert()
                    }
                }
                return
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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled=false

        setUI()
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
        mMap.isMyLocationEnabled = false


        val myLocation = locationManager!!.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (myLocation != null) {
            val yourLocation = LatLng(myLocation.latitude, myLocation.longitude)
            mMap.addMarker(MarkerOptions().position(yourLocation).title("Marker in Sydney"))
            val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
            mMap.animateCamera(update)
        }


        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPS) {
            showSettingsAlert()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
    }

    override fun gotoHome() {
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()
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
            val phone = driverPhoneNumber
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        } catch (e: Exception) {
            // show error message
        }
    }

}