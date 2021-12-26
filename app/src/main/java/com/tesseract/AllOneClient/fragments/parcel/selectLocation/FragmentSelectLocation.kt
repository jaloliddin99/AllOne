package com.tesseract.AllOneClient.fragments.parcel.selectLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentSelectLocationBinding
import com.tesseract.AllOneClient.fragments.taxiCity.main.FragmentCitySelectLocation
import com.tesseract.AllOneClient.utils.bitmapDescriptorFromVector
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*


@AndroidEntryPoint
class FragmentSelectLocation : Fragment(), OnMapReadyCallback {

    private var binding: FragmentSelectLocationBinding? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var isGPS = false
    private var value: Float = 18f
    var locationName:String=""
    lateinit var dialog: Dialog

    private lateinit var viewModel: SelectLocationViewModel
    private val args:FragmentSelectLocationArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectLocationBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

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



        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        binding?.select?.setOnClickListener {
            if (args.fromCity1){
                val action=FragmentSelectLocationDirections.actionGlobalCrudLocation(null,
                args.type1,  latLngFinal,usedSelectedLocation)
                Toast.makeText(context, latLngFinal, Toast.LENGTH_SHORT).show()
                findNavController().navigate(action)
            }else{

                setBackStackData("locationName11", "$usedSelectedLocation###$latLngFinal", true)
            }

        }

    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )
    }


    private var latLngFinal=""
    private var usedSelectedLocation=""

    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(41.0, 69.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

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

        viewModel.data.observe(requireActivity(),  {
            try {
                if (!it.address.isNullOrEmpty()){
                    binding?.textSelect?.text=it.address
                    usedSelectedLocation= it.address!!
                    Log.i(TAG, "onMapReady: $usedSelectedLocation $latLngFinal")
                }else{
                    binding?.textSelect?.text="Not found"
                }
            }catch (e:Exception){

            }
        })


        var latLngVal: LatLng
        var midLatLng = LatLng(0.0, 0.0)
        mMap.setOnCameraIdleListener {
            latLngVal = mMap.cameraPosition.target
            if (latLngVal != midLatLng) {
                askLocation(latLngVal)
            }
            midLatLng = mMap.cameraPosition.target

        }




        viewModel.errorMessage.observe(requireActivity(),{
            dialog.dismiss()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        mMap.isMyLocationEnabled = true

        val myLocation = locationManager!!.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
        if (myLocation != null) {
            val yourLocation = LatLng(myLocation.latitude, myLocation.longitude)
            val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
            mMap.animateCamera(update)
        }

        val locationButton: ImageView =
            (view?.findViewById<View>(Integer.parseInt("1"))?.parent as View).findViewById<View>(
                Integer.parseInt("2")
            ) as ImageView
        locationButton.setImageResource(R.drawable.ic_btn_loc)
        val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        rlp.setMargins(0, 0, 30, 30)

        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPS) {
            turnOnGPS()
            getLocationUpdates()
        } else {
            getDeviceLocation()
        }


    }

    private fun getLocationUpdates() {
        locationRequest.interval = 20000
        locationRequest.fastestInterval = 20000
        locationRequest.smallestDisplacement = 170f //170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //according to your app
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isNotEmpty()) {

                    latLngFinal="${locationResult.lastLocation.latitude},${locationResult.lastLocation.longitude}"
                    askLocation(
                        LatLng(
                            locationResult.lastLocation.latitude,
                            locationResult.lastLocation.longitude
                        )
                    )
                }
            }
        }
    }

    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private fun getDeviceLocation() {
        try {
            val location = mFusedLocationProviderClient.lastLocation

            location.addOnCompleteListener {
                if (it.isSuccessful) {
                    latLngFinal="${it.result.latitude},${it.result.longitude}"
                    moveCamera(LatLng(it.result.latitude, it.result.longitude), 15f)
                    askLocation(LatLng(it.result.latitude, it.result.longitude))
                }
            }

        } catch (e: Exception) {

        }

    }

    private fun turnOnGPS() {
        val request = LocationRequest.create().apply {
            interval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnFailureListener {
            if (it is ResolvableApiException) {
                try {
                    it.startResolutionForResult(requireActivity(), 12345)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }.addOnSuccessListener {
            getDeviceLocation()
        }
    }

    private fun moveCamera(latLng: LatLng, zoom: Float) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    private fun askLocation(latLng: LatLng) {
        viewModel.getLocationReverse(
            headerMapUniversal(requireContext()),
            "${latLng.latitude},${latLng.longitude}"
        )
        value=mMap.cameraPosition.zoom
        val yourLocation = LatLng(latLng.latitude, latLng.longitude)
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
        mMap.animateCamera(update)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    if (!isGPS) {
                        turnOnGPS()
                    }
                    Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
                return
            }
        }
    }


    private fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest()
        locationCallback = object : LocationCallback() {

        }
    }

    override fun onResume() {
        super.onResume()
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
        startLocationUpdates()
    }

    // Start location updates
    private fun startLocationUpdates() {
        mFusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    // Stop location updates
    private fun stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    // Stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }


}