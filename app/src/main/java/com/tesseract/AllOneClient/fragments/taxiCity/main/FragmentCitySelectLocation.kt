package com.tesseract.AllOneClient.fragments.taxiCity.main

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.SearchQueryTypeRegionLocationsAdapter
import com.tesseract.AllOneClient.adapter.taxiCity.CityAddressesHistoryAdapter
import com.tesseract.AllOneClient.databinding.FragmentCityMapBinding
import com.tesseract.AllOneClient.fragments.parcel.selectLocation.SelectLocationViewModel
import com.tesseract.AllOneClient.model.home.LocationSearch.LocationSearchList
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.SavedLocationData
import com.tesseract.AllOneClient.utils.*
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@AndroidEntryPoint
class FragmentCitySelectLocation : Fragment(), OnMapReadyCallback,
    CityAddressesHistoryAdapter.OnLocationClickListener,
    SearchQueryTypeRegionLocationsAdapter.OnItemClickListener {

    private var _binding: FragmentCityMapBinding? = null
    private val binding get() = _binding!!

    private var locationManager: LocationManager? = null
    private lateinit var mMap: GoogleMap
    private var isGPS = false
    private var value: Float = 15F
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    private lateinit var viewModel: CitySelectLocationViewModel
    private lateinit var viewModelReverse: SelectLocationViewModel


    private var startDestination = ""
    private var endDestination = ""
    private var startName = ""
    private var endName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest()
        locationCallback = object : LocationCallback() {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityMapBinding.inflate(inflater, container, false)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
        viewModel = ViewModelProvider(this).get(CitySelectLocationViewModel::class.java)
        viewModelReverse = ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        binding.apply {

            getBackStackData<String>("startDestination", true){
                if (it.length==3){
                    this@FragmentCitySelectLocation.startDestination=""
                    this@FragmentCitySelectLocation.startName=""
                }else{

                    this@FragmentCitySelectLocation.startDestination=it.split("###")[0]
                    this@FragmentCitySelectLocation.startName=it.split("###")[1]

                }
                startDestination.requestFocus()
                startDestination.isPressed=true
                startDestination.setSelection(it.split("###")[1].length)


            }

            getBackStackData<String>("endDestination", true){
                if (it.length==3){
                    this@FragmentCitySelectLocation.endDestination=""
                    this@FragmentCitySelectLocation.endName=""
                }else{
                    this@FragmentCitySelectLocation.endDestination=it.split("###")[0]
                    this@FragmentCitySelectLocation.endName=it.split("###")[1]
                }
                endDestination.requestFocus()
                endDestination.isPressed=true
                endDestination.setSelection(it.split("###")[1].length)
            }


            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.savedLocations(headerMapUniversal(requireContext()))

            viewModel.savedLocations.observe(viewLifecycleOwner, {
                recyclerView.adapter =
                    CityAddressesHistoryAdapter(it, this@FragmentCitySelectLocation)
            })

            viewModel.locationList.observe(viewLifecycleOwner, {
                try {
                    recyclerView.adapter =
                        SearchQueryTypeRegionLocationsAdapter(it, this@FragmentCitySelectLocation)
                } catch (e: Exception) {

                }
            })

            savedLocations.setOnClickListener {
                val action = FragmentCitySelectLocationDirections.actionGlobalSavedLocationList()
                findNavController().navigate(action)
            }
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            goToTariffs.setOnClickListener {
                if (this@FragmentCitySelectLocation.startDestination.isNotEmpty() &&
                    startName.isNotEmpty()
                ) {

                    val action =
                        FragmentCitySelectLocationDirections.actionFragmentCityMapToFragmentCityOrderMaps(
                            this@FragmentCitySelectLocation.startDestination,
                            this@FragmentCitySelectLocation.endDestination, startName, endName
                        )
                    findNavController().navigate(action)

                } else {
                    Toast.makeText(context, "Please, select location first", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            selectFromMap.setOnClickListener {
                view.hideKeyboard()
                selectFromMap.visibility=View.GONE
            }
        }

        bottomSheetListeners()
        keyboardListener()
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun keyboardListener() {
        KeyboardVisibilityEvent.setEventListener(
            requireActivity(),
            object : KeyboardVisibilityEventListener {
                override fun onVisibilityChanged(isOpen: Boolean) {
                    try {
                        if (isOpen) {
                            binding.selectFromMap.visibility=View.VISIBLE
                            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED

                            if (binding.endDestination.hasFocus()){
                                binding.bottomSHeet.visibility = View.GONE
                            }

                            binding.backToHome.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                            binding.yourAddress.visibility = View.GONE
                            if (binding.startDestination.hasFocus()) {
                                binding.yourAddress1.text = getString(R.string.where_)
                            } else if (binding.endDestination.hasFocus()) {
                                binding.yourAddress1.text = getString(R.string.to_where_)
                            }
                            binding.yourAddress1.setTextColor(requireContext().getColor(R.color.white))
                        } else {
                            mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                            binding.bottomSHeet.visibility = View.VISIBLE
                            binding.backToHome.setColorFilter(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.black
                                ), android.graphics.PorterDuff.Mode.SRC_IN
                            )
                            binding.yourAddress1.setTextColor(requireContext().getColor(R.color.black))
                        }
                    } catch (e: Exception) {

                    }

                }
            })

    }


    private fun bottomSheetListeners() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val bottomSheet: View = requireView().findViewById(R.id.bottomSheetNestedScrollView)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {

            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                binding.txtFromCard.alpha = slideOffset
                binding.requestFocus.alpha = 1 - slideOffset
                binding.blur.alpha = slideOffset
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val sydney = LatLng(41.00, 69.00)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16f))
        mMap.uiSettings.isCompassEnabled = false


        binding.startDestination.addTextChangedListener(startDestinationTextWatcher)
        binding.endDestination.addTextChangedListener(startDestinationTextWatcher2)

        viewModelReverse.data.observe(viewLifecycleOwner, {


            when {
                binding.startDestination.hasFocus() -> {
                    startName = it.address.toString()
                    binding.startDestination.setText(it.address)
                    binding.startDestination.isPressed=true
                    binding.startDestination.setSelection(binding.startDestination.text.toString().length)
                }
                binding.endDestination.hasFocus() -> {
                    endName = it.address.toString()
                    binding.endDestination.setText(it.address)
                    binding.endDestination.isPressed=true
                    binding.endDestination.setSelection(binding.endDestination.text.toString().length)
                }
                else -> {
                    startName = it.address.toString()
                    binding.startDestination.setText(it.address)
                    binding.startDestination.isPressed=true
                    binding.startDestination.setSelection(binding.startDestination.text.toString().length)
                }
            }

            binding.yourAddress1.text = it.address
            binding.shimmerLayout.stopShimmer()

        })

        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPS) {
            turnOnGPS()
            getLocationUpdates()
        } else {
            getDeviceLocation()
        }
        binding.requestFocus.setOnClickListener {

            getDeviceLocation()
        }

        var latLngVal: LatLng
        var midLatLng = LatLng(0.0, 0.0)
        mMap.setOnCameraIdleListener {
            latLngVal = mMap.cameraPosition.target
            if (latLngVal != midLatLng) {
                askLocation(latLngVal)
            }
            midLatLng = mMap.cameraPosition.target

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
                    moveCamera(LatLng(it.result.latitude, it.result.longitude), 15f)
                    askLocation(LatLng(it.result.latitude, it.result.longitude))
                }
            }

        } catch (e: java.lang.Exception) {

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
        binding.shimmerLayout.startShimmer()
        viewModelReverse.getLocationReverse(
            headerMapUniversal(requireContext()),
            "${latLng.latitude},${latLng.longitude}"
        )
        when {
            binding.startDestination.hasFocus() -> {
                startDestination = "${latLng.latitude},${latLng.longitude}"
            }
            binding.endDestination.hasFocus() -> {
                endDestination = "${latLng.latitude},${latLng.longitude}"
            }
            else -> {
                startDestination = "${latLng.latitude},${latLng.longitude}"
            }
        }
        value=mMap.cameraPosition.zoom
        val yourLocation = LatLng(latLng.latitude, latLng.longitude)
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
        mMap.animateCamera(update)

    }

    override fun onItemClick(type: SavedLocationData) {
        if (!binding.endDestination.hasFocus()) {
            binding.startDestination.setText(type.address)
            startDestination = type.latLng.toString()
            startName = type.address.toString()

            binding.endDestination.requestFocus()
        } else {
            endDestination = type.latLng.toString()
            binding.endDestination.setText(type.address)
            endName = type.address.toString()
        }

    }

    override fun onItemClick2(position: Int, districtName: LocationSearchList) {
        if (binding.endDestination.hasFocus()) {
            binding.endDestination.setText(districtName.address)
            endDestination = districtName.lat + "," + districtName.lon
            endName = districtName.address.toString()
            requireContext().hideKeyboard(requireView())
        }
        if (binding.startDestination.hasFocus()) {
            startDestination = districtName.lat + "," + districtName.lon
            startName = districtName.address.toString()

            binding.startDestination.setText(districtName.address)
            binding.endDestination.requestFocus()
        }
    }

    private val startDestinationTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isEmpty()) {
                viewModel.savedLocations(headerMapUniversal(requireContext()))
            } else {
                viewModel.getLocationSearch(headerMapUniversal(requireContext()), s.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private val startDestinationTextWatcher2 = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().isEmpty()) {
                viewModel.savedLocations(headerMapUniversal(requireContext()))
            } else {
                viewModel.getLocationSearch(headerMapUniversal(requireContext()), s.toString())
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    private fun <T> Fragment.getBackStackData(
        key: String,
        singleCall: Boolean = true,
        result: (T) -> (Unit)
    ) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                result(it)
                if (singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(
                    key
                )
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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