package com.tesseract.AllOneClient.fragments.order.mapActiveCity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentCityActiveMapBinding
import com.tesseract.AllOneClient.dialogs.city.CityOrderFinished
import com.tesseract.AllOneClient.dialogs.commonDialogs.driverRating.DialogDriverRating
import com.tesseract.AllOneClient.utils.statusBarColor


class FragmentCityActiveMap : Fragment(), CityOrderFinished.OnLickListener, DialogDriverRating.GoToHomeListener {

    private var binding: FragmentCityActiveMapBinding? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(41.0, 69.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityActiveMapBinding.inflate(inflater, container, false)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            false
        )
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        val bottomSheet: View = view.findViewById(R.id.bottomSheetNestedScrollView)

        binding?.driver?.setOnClickListener {
            val action = FragmentCityActiveMapDirections.actionGlobalCancelOrder(2, "city")
            findNavController().navigate(action)
        }

        binding?.paymentMethod?.setOnClickListener {

            Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()
            val action = FragmentCityActiveMapDirections.actionMapsFragmentToFragmentCityPaymentMethod()
            findNavController().navigate(action)
        }

        binding?.topLinear?.cancel?.setOnClickListener {


        }


        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {



            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding?.topLinearLayout?.alpha = 1f

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding?.topLinearLayout?.alpha = 0f
                        binding?.chattingOptionUI?.alpha = 0f

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        binding?.chattingOptionUI?.alpha = 0f
                    }

                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

                binding?.topLinearLayout?.alpha = 1f - slideOffset
                binding?.bottomSheetBackgroundLayer?.alpha = slideOffset

                if (slideOffset < 0.5f) {
                    binding?.chattingOptionUI?.alpha = 2f * (0.5f - slideOffset)
                }
            }
        })

        val height: Float? = binding?.chattingOptionUI?.height?.toFloat()
        var isAnimated: Boolean = false

        binding?.chattingOptionUI?.translationY = -height!!

        binding?.topLinear?.write?.setOnClickListener {
            if (!isAnimated) {
                binding?.chattingOptionUI?.animate()?.translationY(height)?.duration = 500
                isAnimated = true
            } else {
                binding?.chattingOptionUI?.animate()?.translationY(-height)?.duration = 500
                isAnimated = false
            }
        }
    }

    override fun rate() {
        val menuFragment = DialogDriverRating("city", 1, 1,this)
        menuFragment.isCancelable = false
        menuFragment.show(parentFragmentManager, menuFragment.tag)
    }

    override fun aboutTrip() {

    }


    override fun gotoHome() {
        val action = FragmentCityActiveMapDirections.actionGlobalComposeFragment()
        findNavController().navigate(action)
    }
}