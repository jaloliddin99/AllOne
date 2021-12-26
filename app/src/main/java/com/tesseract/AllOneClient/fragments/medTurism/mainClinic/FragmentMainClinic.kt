package com.tesseract.AllOneClient.fragments.medTurism.mainClinic

import android.Manifest
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.*
import com.tesseract.AllOneClient.databinding.FragmentMedTurizmEntranceBinding
import com.tesseract.AllOneClient.fragments.taxiCity.main.FragmentCitySelectLocation
import com.tesseract.AllOneClient.model.medTourism.MainMedModel
import com.tesseract.AllOneClient.model.medTourism.medMain.NearbyClinic
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularCategory
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularClinic
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularDoctor
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMainClinic : Fragment(),
    ClinicMainAdapter.OnImageClickListener, ChipAdapter.OnChipClickListener,
    PopularAdapter.PopularClinics, PopularDoctorAdapter.OnDoctorClicked, NearClinicsAdapter.OnNearByKlicked {
    private var _binding: FragmentMedTurizmEntranceBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainClinicViewModel
    private var isGPS = false
    private var locationManager: LocationManager? = null
    private lateinit var mainMedModel: List<MainMedModel>
    private lateinit var clinicMainAdapter: ClinicMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMedTurizmEntranceBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(MainClinicViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        loadItems()

        clinicMainAdapter=
            ClinicMainAdapter(mainMedModel, this)
        binding.recyclerView.adapter=clinicMainAdapter
        binding.recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        binding.recyclerView.setHasFixedSize(true)




        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        binding.ambulance.setOnClickListener {
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentMedAmbulance()
            findNavController().navigate(action)
        }

        binding.apply {

            viewModel.mainIndex.observe(viewLifecycleOwner, {
                recyclerViewChip.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter= ChipAdapter(this@FragmentMainClinic, it.content.popular_categories)
                }
                loader.loader.visibility=View.GONE


                recyclerViewPopular.apply {
                    layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                            lp.width = width * 6 / 8
                            return true
                        }
                    }
                    adapter= PopularAdapter(this@FragmentMainClinic, it.content.popular_clinics)
                }


                recyclerViewDoctors.apply {
                    layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                            lp.width = width * 3 / 8
                            return true
                        }
                    }
                    adapter= PopularDoctorAdapter(this@FragmentMainClinic, it.content.popular_doctors)
                }

                recyclerViewNear.apply {
                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter= NearClinicsAdapter(this@FragmentMainClinic, it.content.nearby_clinics)
                }
            })

        }
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        isGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!isGPS) {
            turnOnGPS()
            getLocationUpdates()
        } else {
            getDeviceLocation()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest()
        locationCallback = object : LocationCallback() {

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
                    Toast.makeText(
                        context,
                        "${locationResult.lastLocation.latitude.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.mainIndex(headerMapUniversal(requireContext()), locationResult.lastLocation.latitude.toString(), locationResult.lastLocation.longitude.toString())

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
                    viewModel.mainIndex(headerMapUniversal(requireContext()), it.result.latitude.toString(), it.result.longitude.toString())
                }
            }

        } catch (e: java.lang.Exception) {

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            FragmentCitySelectLocation.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

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

    private fun loadItems(){
        mainMedModel= listOf(
            MainMedModel(getString(R.string.clinic), R.drawable.ic_clinics),
            MainMedModel(getString(R.string.categories), R.drawable.ic_clinic_world),
            MainMedModel(getString(R.string.doctors), R.drawable.ic_clinic_gadget),
            MainMedModel(getString(R.string.mySaved), R.drawable.ic_saved),
        )
    }

    override fun onItemClick(position: Int) {
        if (position==0){
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentClinics()
            findNavController().navigate(action)
        }
        if (position==0){
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentClinics()
            findNavController().navigate(action)
        }

        if (position==2){
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentMedTurDoctors()
            findNavController().navigate(action)
        }
        if (position==3){
            val action=FragmentMainClinicDirections.actionGlobalMedOrTourFavourites("med_tourism")
            findNavController().navigate(action)
        }
    }

    override fun onChipClicked(position: PopularCategory) {

    }

    override fun onPopularClicked(position: PopularClinic) {
        val action=FragmentMainClinicDirections.actionFragmentClinicsToFragmentClinicInfo(position.id)
        findNavController().navigate(action)
    }

    override fun onChipClicked(position: PopularDoctor) {
        val action=FragmentMainClinicDirections.actionFragmentMedTurDoctorsToFragmentDoctorView(position.id)
        findNavController().navigate(action)
    }

    override fun onChipClicked(position: NearbyClinic) {
        val action=FragmentMainClinicDirections.actionFragmentClinicsToFragmentClinicInfo(position.id)
        findNavController().navigate(action)
    }


    override fun onResume() {
        super.onResume()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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



    companion object {
        private const val MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

}