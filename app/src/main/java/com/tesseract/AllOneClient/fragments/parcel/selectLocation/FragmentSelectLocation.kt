package com.tesseract.AllOneClient.fragments.parcel.selectLocation

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
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
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentSelectLocationBinding
import com.tesseract.AllOneClient.utils.bitmapDescriptorFromVector
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class FragmentSelectLocation : Fragment(), OnMapReadyCallback {

    private var binding: FragmentSelectLocationBinding? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var isGPS = false
    private val value: Float = 10F
    var locationName:String=""
    lateinit var dialog: Dialog

    private lateinit var viewModel: SelectLocationViewModel
    private val args:FragmentSelectLocationArgs by navArgs()

    private var marker: Marker? = null

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
            if (args.fromCity){
                val action=FragmentSelectLocationDirections.actionGlobalCrudLocation(null,
                args.type,  latLngFinal,usedSelectedLocation)
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

    override fun onResume() {
        super.onResume()
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
    }

    private var latLngFinal=""
    private var usedSelectedLocation=""

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

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
            dialog.dismiss()
            if (!it.address.isNullOrEmpty()){
                binding?.textSelect?.text=it.address
                usedSelectedLocation= it.address!!

                Log.i(TAG, "onMapReady: $usedSelectedLocation $latLngFinal")
            }else{
                binding?.textSelect?.text="Not found"
            }
        })


        viewModel.errorMessage.observe(requireActivity(),{
            dialog.dismiss()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        mMap.setOnMapClickListener {
            marker?.remove()
            loader()
            latLngFinal = it.latitude.toString() + "," + it.longitude.toString()
            viewModel.getLocationReverse(headerMapUniversal(requireContext()), latLngFinal)

            marker = mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(it.latitude, it.longitude))
                    .draggable(true).visible(true).title(locationName)
                    .icon(context?.bitmapDescriptorFromVector(R.drawable.ic_dest))
            )
            val yourLocation = LatLng(it.latitude, it.longitude)
            val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
            mMap.animateCamera(update)

        }

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
            showSettingsAlert()
        }

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
                        showSettingsAlert()
                    }
                }
                return
            }
        }
    }


    private fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.gps_not_enabled))
        alertDialog.setMessage(getString(R.string.doyou_want_turnOn))
        alertDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        alertDialog.setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
        alertDialog.show()
    }

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
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