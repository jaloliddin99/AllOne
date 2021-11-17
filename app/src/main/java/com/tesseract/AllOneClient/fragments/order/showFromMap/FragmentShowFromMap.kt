package com.tesseract.AllOneClient.fragments.order.showFromMap

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentShowFromMapBinding
import com.tesseract.AllOneClient.fragments.parcel.selectLocation.SelectLocationViewModel
import com.tesseract.AllOneClient.utils.bitmapDescriptorFromVector
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentShowFromMap : Fragment(), OnMapReadyCallback {
    val args: FragmentShowFromMapArgs by navArgs()
    lateinit var dialog: Dialog
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private lateinit var mMap: GoogleMap
    private var MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var isGPS = false
    private val value: Float = 16F
    private lateinit var viewModel: SelectLocationViewModel
    var binding: FragmentShowFromMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShowFromMapBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SelectLocationViewModel::class.java)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)




        binding?.back?.setOnClickListener {
            findNavController().popBackStack()
        }

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        loader()
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

        viewModel.getLocationReverse(headerMapUniversal(requireContext()), args.latLng)

        val lat = args.latLng.split(",")[0].toDouble()
        val lng = args.latLng.split(",")[1].toDouble()
        val yourLocation = LatLng(lat, lng)
        val update: CameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, value)
        mMap.animateCamera(update)


        val markerOptions: MarkerOptions = MarkerOptions().position(yourLocation)
            .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_dest))

        viewModel.data.observe(requireActivity(), {
            dialog.dismiss()
            if (!it.address.isNullOrEmpty()) {
                binding?.locationWithDetail?.text = it.address
                binding?.locationGlobal?.text = it.region
                mMap.addMarker(markerOptions)
            }
        })

        viewModel.errorMessage.observe(requireActivity(), {
            dialog.dismiss()
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })



        mMap.isMyLocationEnabled = true



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
                } else {

                }
                return
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

    private fun loader() {
        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


}