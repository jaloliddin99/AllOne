package com.tesseract.AllOneClient.fragments.main.home.routeTariffs

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
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
import com.tesseract.AllOneClient.dialogs.main.DialogTariffNotFound
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
class FragmentTaxiRegionsSelection : Fragment(),
    TariffAdapter.DialogCloseListener {

    private var tariffText: String = ""
    private var _binding: FragmentTarifBinding? = null
    private val binding get() = _binding!!

    private lateinit var tariffAdapter: TariffAdapter
    private lateinit var tariffModel: List<RouteTariffContentListModel>

    private var isEnabled: Boolean = false
    private var isClickEventEnabled: Boolean = true
    private lateinit var viewModel: RouteTariffViewModel
    private val shareViewModel: ShareViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTarifBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(RouteTariffViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("addressName")?.observe(
            viewLifecycleOwner) {result ->
            binding.startDestination.text=result
            binding.startDestinationChange.text = getString(R.string.change)
        }
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("addressName1")?.observe(
            viewLifecycleOwner) {result ->
            binding.endDestination.text=result
            binding.endDestinationTextChange.text = getString(R.string.change)
        }

        if (Common.startRegionId.isNotEmpty() && Common.endRegionId.isNotEmpty()
            && Common.startDistrictId.isNotEmpty() && Common.endDistrictId.isNotEmpty()
        ) {
            binding.apply {
                startDestinationChange.text = getString(R.string.change)
                endDestinationTextChange.text = getString(R.string.change)
                relativeView.visibility=View.GONE
                btnTariffOrder.text = getString(R.string.continue_)
                btnTariffOrder.backgroundTintList = context?.getColorStateList(R.color.dark_grey)
                if (Common.startRegionId=="14"){
                    startDestination.text = Common.startRegion
                }else{
                    startDestination.text = Common.startRegion + " " +Common.startDistrict
                }

                if (Common.endRegionId=="14"){
                    endDestination.text = Common.endRegion
                }else{
                    endDestination.text = Common.endRegion + " " + Common.endDistrict
                }

                viewModel.getRouteTariff(headerMapUniversal(requireContext()),Common.startDistrictId, Common.endDistrictId)
            }
        }

        var dialogShowing=false
        binding.apply {
            var dialog=DialogFragment()
            viewModel.routeList.observe(viewLifecycleOwner, Observer {
                if (it.isEmpty()){
                    if (!dialogShowing){
                        dialog=DialogTariffNotFound("${Common.startRegion} ${Common.startDistrict}","${Common.endRegion} ${Common.endDistrict}")
                        dialog.show(parentFragmentManager, tag)
                        dialogShowing=true
                    }
                }else{
                    if (dialogShowing){
                        dialog.dismiss()
                    }
                    recyclerTariff.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    tariffAdapter = TariffAdapter(requireContext(), it, this@FragmentTaxiRegionsSelection)
                    recyclerTariff.adapter = tariffAdapter
                }
            })
            recyclerTariff.setHasFixedSize(true)
            isEnabled = false
            btnTariffOrder.backgroundTintList = context?.getColorStateList(R.color.green)


        }


        binding.backToHome.setOnClickListener {
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

        if (Common.destination == 0) {
            if (Common.startRegionId.isNotEmpty() && Common.startDistrictId.isNotEmpty()) {
                binding.startDestination.text = "${Common.startRegion} ${Common.startDistrict}"
                binding.startDestinationChange.text = getString(R.string.change)
            }
        }
        if (Common.destination == 1) {
            if (Common.endRegionId.isNotEmpty() && Common.endDistrictId.isNotEmpty()) {
                binding.endDestination.text = "${Common.endRegion} ${Common.endDistrict}"
                binding.endDestinationTextChange.text = getString(R.string.change)
            }
        }

        tariffOrder()

        Common.isCurrentRegionFragment = true

        binding.startDestinationChange.setOnClickListener {
            Common.destination=0
            dialogShowing=false
            val action = FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentRegions()
            findNavController().navigate(action)
        }

        binding.endDestinationTextChange.setOnClickListener {
            Common.destination=1
            dialogShowing=false
            val action = FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentRegions()
            findNavController().navigate(action)

        }


        getBackStackData<RegionPopbackStask>("regionList", true){

            if (it.direction == 0) {
                Common.startDistrictId=0.toString()
                binding.startDestination.text = "${it.locationName}"
                binding.startDestinationChange.text = getString(R.string.change)
            }
            if (it.direction  == 1) {
                Common.endDistrictId=0.toString()
                binding.endDestination.text = "${it.locationName}"
                binding.endDestinationTextChange.text = getString(R.string.change)
            }
        }
    }

    override fun onDialogClose(tariff: String?, name: String?, url: String?, price: String?) {
        if (tariff != null) {
            tariffText = tariff
            Log.i(TAG, "onDialogClose: $tariff")
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

        binding.apply {
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
        binding.btnTariffOrder.setOnClickListener {
            if (isClickEventEnabled) {
                if (!isEnabled) {

                } else {
                    val action =
                        FragmentTaxiRegionsSelectionDirections.actionFragmentTaxiRegionsToFragmentOrderTaxi2(if (tariffText=="standard") 1 else if (tariffText=="women") 2 else 3)
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}







