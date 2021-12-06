package com.tesseract.AllOneClient.fragments.taxiCity.stations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.taxiCity.StationAdapter
import com.tesseract.AllOneClient.databinding.FragmentCityNewStationsBinding
import com.tesseract.AllOneClient.model.taxiCity.StationModel

class FragmentStations : Fragment(R.layout.fragment_city_new_stations) , StationAdapter.OnDeleteListener{

    private var _binding: FragmentCityNewStationsBinding?=null
    private val binding get() = _binding!!
    private val args:FragmentStationsArgs by navArgs()

    private lateinit var stationAdapter: StationAdapter
    private  var stationModel= ArrayList<StationModel>()
    private var isFirst=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCityNewStationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            startAddress.text=args.startDestination
            endAddress.text=args.endDestination
            stationModel=args.stationList.stationModel as ArrayList<StationModel>

            requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        Common.isCityTariffPreviousBackstack=true
                        setBackStackData("CityNewStations", stationModel, true)
                    }
                })
            backToHome.setOnClickListener {
                Common.isCityTariffPreviousBackstack=true
                setBackStackData("CityNewStations", stationModel, true)

            }
            newStation.setOnClickListener {
                val action=FragmentStationsDirections.actionGlobalLocationReverse(false, "")
                findNavController().navigate(action)
            }


            getBackStackData<String>("locationName11", true){
                stationModel.add(StationModel(it.split("###")[1], it.split("###")[0]))
                stationAdapter.notifyItemInserted(stationModel.lastIndex)
                isFirst=false
            }

        }

            binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            stationAdapter= StationAdapter(stationModel, this)
            binding.recyclerView.adapter=stationAdapter
            binding.recyclerView.setHasFixedSize(true)

    }


    override fun onDelete(position: Int) {
        Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
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

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}