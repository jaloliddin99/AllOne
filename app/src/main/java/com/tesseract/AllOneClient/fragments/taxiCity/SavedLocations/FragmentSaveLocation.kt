package com.tesseract.AllOneClient.fragments.taxiCity.SavedLocations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.taxiCity.AdapterSavedLocations
import com.tesseract.AllOneClient.databinding.FragmentSaveLocationBinding
import com.tesseract.AllOneClient.fragments.taxiCity.main.CitySelectLocationViewModel
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.SavedLocationData
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentSaveLocation : Fragment(), AdapterSavedLocations.OnLocationClickListener {


    var _binding: FragmentSaveLocationBinding? = null
    private val binding get() = _binding!!
    private lateinit var cityMainViewModel: CitySelectLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaveLocationBinding.inflate(inflater, container, false)
        cityMainViewModel = ViewModelProvider(this).get(CitySelectLocationViewModel::class.java)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, com.tesseract.AllOneClient.R.color.white, requireActivity().theme),
            true
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.backToHome.setOnClickListener {
            val action=FragmentSaveLocationDirections.actionFragmentSaveLocationToFragmentCityMap()
            findNavController().navigate(action)
        }
        binding.backTo.setOnClickListener {
            val action=FragmentSaveLocationDirections.actionFragmentSaveLocationToFragmentCityMap()
            findNavController().navigate(action)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action=FragmentSaveLocationDirections.actionFragmentSaveLocationToFragmentCityMap()
                    findNavController().navigate(action)
                }
            }
            )

        var savedLocationDataHome:SavedLocationData?=null
        var savedLocationDataWork:SavedLocationData?=null
        var savedLocationData: ArrayList<SavedLocationData>
        binding.apply {
            cityMainViewModel.savedLocations(headerMapUniversal(requireContext()))
            cityMainViewModel.savedLocations.observe(viewLifecycleOwner, {
                loader.loader.visibility=View.GONE
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                savedLocationData=it as ArrayList<SavedLocationData>

                if (savedLocationData.size>0){
                    if (savedLocationData[0].type=="home"){
                        savedLocationDataHome=savedLocationData[0]
                        savedLocationData.removeAt(0)
                        homeAddEditIcon.setImageResource(com.tesseract.AllOneClient.R.drawable.ic_edit_penn)
                        homeAddress.text=savedLocationDataHome?.name
                    }
                }
                if (savedLocationData.size>0){
                    if (savedLocationData[0].type=="work"){
                        savedLocationDataWork=savedLocationData[0]
                        savedLocationData.removeAt(0)
                        workAddEditIcon.setImageResource(com.tesseract.AllOneClient.R.drawable.ic_edit_penn)
                        workplace.text=savedLocationDataWork?.name
                    }
                }


                recyclerView.adapter = AdapterSavedLocations(savedLocationData, this@FragmentSaveLocation)

            })

            addOtherAddresses.setOnClickListener {
                val action=FragmentSaveLocationDirections.actionGlobalLocationReverse(true, "custom")
                findNavController().navigate(action)
            }

            homeAddress.setOnClickListener {
                if (savedLocationDataHome!=null) {
                    val action =
                        FragmentSaveLocationDirections.actionGlobalCrudLocation(savedLocationDataHome, type="", latLng="", address="")
                    findNavController().navigate(action)
                }else{
                    val action=FragmentSaveLocationDirections.actionGlobalLocationReverse(fromCity1 = true, type1 = "home")
                    findNavController().navigate(action)
                }
            }

            workplace.setOnClickListener {
                if (savedLocationDataWork!=null) {
                    val action =
                        FragmentSaveLocationDirections.actionGlobalCrudLocation(savedLocationDataWork, type="", latLng="", address="")
                    findNavController().navigate(action)
                }else{
                    val action=FragmentSaveLocationDirections.actionGlobalLocationReverse(fromCity1 = true, type1 = "work")
                    findNavController().navigate(action)
                }
            }
        }

    }

    override fun onItemClick(type: SavedLocationData?) {
        val action = FragmentSaveLocationDirections.actionGlobalCrudLocation(type, type="", latLng="", address="")
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}