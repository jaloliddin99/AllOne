package com.tesseract.AllOneClient.fragments.main.home.routeTariffs

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.TariffAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentTarifBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import com.tesseract.AllOneClient.model.home.getRegions.RegionPopbackStask
import com.tesseract.AllOneClient.model.home.getRegions.ShareRegionDistrict
import com.tesseract.AllOneClient.model.home.routeRariffs.RouteTariffContentListModel
import com.tesseract.AllOneClient.model.home.routeRariffs.ShareModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTaxiRegionsSelection
    : Fragment(R.layout.fragment_tarif),
    TariffAdapter.DialogCloseListener {

    private var tariffText: String = ""
    private var binding: FragmentTarifBinding? = null

    private lateinit var tariffAdapter: TariffAdapter
    private lateinit var tariffModel: List<RouteTariffContentListModel>

    private var isEnabled: Boolean = false
    private var isClickEventEnabled: Boolean = true
    private lateinit var viewModel: RouteTariffViewModel
    private val shareViewModel: ShareViewModel by activityViewModels()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tariffBinding = FragmentTarifBinding.bind(view)

        binding = tariffBinding

        viewModel = ViewModelProvider(this).get(RouteTariffViewModel::class.java)

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("addressName")?.observe(
            viewLifecycleOwner) {result ->
            binding!!.startDestination.text=result
            binding!!.startDestinationChange.text = getString(R.string.change)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("addressName1")?.observe(
            viewLifecycleOwner) {result ->
            binding!!.endDestination.text=result
            binding!!.endDestinationTextChange.text = getString(R.string.change)
        }



        binding?.apply {
            viewModel.routeList.observe(requireActivity(), Observer {
                tariffAdapter =
                    TariffAdapter(requireContext(), it, this@FragmentTaxiRegionsSelection)
                recyclerTariff.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                recyclerTariff.adapter = tariffAdapter
            })
            recyclerTariff.setHasFixedSize(true)
            isEnabled = false
            btnTariffOrder.backgroundTintList = context?.getColorStateList(R.color.green)

        }


        binding?.backToHome?.setOnClickListener {
            val action = FragmentTaxiRegionsSelectionDirections.actionGlobalComposeFragment()
            findNavController().navigate(action)
        }
        isClickEventEnabled = false
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        FragmentTaxiRegionsSelectionDirections.actionGlobalComposeFragment()
                    findNavController().navigate(action)
                }
            })

//        startRegion = args.startRegionName
//        startDistrict = args.startDistrictName
//        endRegion = args.endRegionName
//        endDistrict = args.endDistrictName
//
//        startRegionId = args.startRegionId
//        startDistrictId = args.startDistrictId
//        endRegionId = args.endRegionId
//        endDistrictId = args.endDistrictId
//
//        if (startRegion.contains("myLocationStartKey")){
//            binding?.startDestination?.text=startRegion.removePrefix("myLocationStartKey")
//        }else if(startRegion.contains("myLocationEndKey")){
//            binding?.endDestination?.text=startRegion.removePrefix("myLocationEndKey")
//
//        }


        if (Common.destination == 0) {
            if (Common.startRegionId.isNotEmpty() && Common.startDistrictId.isNotEmpty()) {
                binding!!.startDestination.text = "${Common.startRegion} ${Common.startDistrict}"
                binding!!.startDestinationChange.text = getString(R.string.change)
            }
        }
        if (Common.destination == 1) {
            if (Common.endRegionId.isNotEmpty() && Common.endDistrictId.isNotEmpty()) {
                binding!!.endDestination.text = "${Common.endRegion} ${Common.endDistrict}"
                binding!!.endDestinationTextChange.text = getString(R.string.change)
            }
        }

        tariffOrder()

        if (Common.startRegionId.isNotEmpty() && Common.endRegionId.isNotEmpty()
            && Common.startDistrictId.isNotEmpty() && Common.endDistrictId.isNotEmpty()
        ) {
            binding?.apply {
                startDestinationChange.text = getString(R.string.change)
                endDestinationTextChange.text = getString(R.string.change)
                relativeView.visibility=View.GONE
                btnTariffOrder.text = "Продолжить"
                btnTariffOrder.backgroundTintList = context?.getColorStateList(R.color.dark_grey)
                startDestination.text = Common.startRegion + " " +Common.startDistrict
                endDestination.text = Common.endRegion + " " + Common.endDistrict
                viewModel.getRouteTariff(headerMapUniversal(requireContext()),Common.startDistrictId, Common.endDistrictId)
            }
        }
        Common.isCurrentRegionFragment = true

        binding!!.startDestinationChange.setOnClickListener {
            Common.destination=0
            val action =
                FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentRegions()
            findNavController().navigate(action)
        }

        binding!!.endDestinationTextChange.setOnClickListener {
            Common.destination=1
            val action =
                FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentRegions()
            findNavController().navigate(action)

        }


        getBackStackData<RegionPopbackStask>("regionList", true){

            if (it.direction == 0) {
                Common.startDistrictId=0.toString()
                binding!!.startDestination.text = "${it.locationName}"
                binding!!.startDestinationChange.text = getString(R.string.change)
            }
            if (it.direction  == 1) {
                Common.endDistrictId=0.toString()
                binding!!.endDestination.text = "${it.locationName}"
                binding!!.endDestinationTextChange.text = getString(R.string.change)
            }
        }
    }

    override fun onDialogClose(tariff: String?, name: String?, url: String?, price: String?) {
        if (tariff != null) {
            tariffText = tariff
            val model=ShareModel(
                tariffText,
                name,
                Common.startDistrictId,
                Common.endDistrictId,
                url,
                price
            )
            shareViewModel.selectItem(model)
        }

        binding?.apply {
            btnTariffOrder.backgroundTintList = context?.getColorStateList(R.color.green)
            btnTariffOrder.elevation = 0F
            if (Common.startRegionId.isNotEmpty() && Common.endRegionId.isNotEmpty()
                && Common.startDistrictId.isNotEmpty() && Common.endDistrictId.isNotEmpty()
            ) {
                isEnabled = true
            }
            isClickEventEnabled = true
        }
    }


    private fun tariffOrder() {
        binding?.btnTariffOrder?.setOnClickListener {
            if (isClickEventEnabled) {
                if (!isEnabled) {

                } else {
                    val action =
                        FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentOrderTaxi2()
                    findNavController().navigate(action)
                }
            } else {
                val action =
                    FragmentTaxiRegionsSelectionDirections.actionGlobalComposeFragment()
                findNavController().navigate(action)
            }
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

}







