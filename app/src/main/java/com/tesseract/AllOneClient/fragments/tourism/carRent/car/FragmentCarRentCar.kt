package com.tesseract.AllOneClient.fragments.tourism.carRent.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.tourism.carRent.CarRentCarsAdapter
import com.tesseract.AllOneClient.adapter.tourism.carRent.CarRentSelectedItems
import com.tesseract.AllOneClient.databinding.FragmentCarRentCarsBinding
import com.tesseract.AllOneClient.fragments.tourism.tourPackages.FragmentTourismPackagesDirections
import com.tesseract.AllOneClient.model.tourism.carRent.cars.Data
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentCarRentCar:Fragment(), CarRentCarsAdapter.OnChipClickListener {
    private var _binding:FragmentCarRentCarsBinding?=null

    private val binding get() = _binding!!
    private lateinit var adapter: CarRentCarsAdapter

    private val args:FragmentCarRentCarArgs by navArgs()

    private lateinit var viewModel: CarViewModel
    val mapKeys = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentCarRentCarsBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CarViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currencyContent.id!=-10){
            binding.currency.text=currencyContent.name
        }
        if (countryContent.id!=-10){
            binding.countryName.text=countryContent.name
        }
        binding.recyclerViewChip.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        Common.carRentPageId=1
        viewModel.startCarViewModel(headerMapUniversal(requireContext()), "", -10, -10, "", mapKeys, args.carId)

        adapter=CarRentCarsAdapter(this, mutableSetOf())
        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            loader.loader.visibility = View.VISIBLE
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView1.adapter = adapter
            recyclerView1.layoutManager = layoutManager2
            recyclerView1.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.carRentRequest(headerMapUniversal(requireContext()), "", countryContent.id, currencyContent.id, "", null, args.carId)
                }
            })

        }
        adapterSet()

        binding.apply {

            first.setOnClickListener {
                direct(10)
            }
            second.setOnClickListener {
                direct(11)
            }

            countryName.setOnClickListener {
                direct(2)
            }
            currency.setOnClickListener {
                direct(3)
            }
        }


        getBackStackData<Int>("cancelledInfo", true) {
            load()
        }

        getBackStackData<Content>("onCountrySelectKey", true) {
            binding.countryName.text = it.name
            countryContent = it
            if (currencyContent.id != -10) {
                binding.currency.text = currencyContent.name
            }

            load()

        }

        getBackStackData<Content>("onCurrencySelectedKey", true) {
            binding.currency.text = it.name
            currencyContent = it
            if (countryContent.id != -10) {
                binding.countryName.text = countryContent.name
            }
            load()
        }

        getBackStackData<Content>("onCarCompaniesSelected", true) {
            carCompanies = it


            if (countryContent.id != -10) {
                binding.countryName.text = countryContent.name
            }
            if (currencyContent.id != -10) {
                binding.currency.text = currencyContent.name
            }

            if (carModels.id!=-10){
                val list= arrayListOf(
                    carCompanies,
                    carModels
                )
                binding.recyclerViewChip.adapter=CarRentSelectedItems(list)
            }else{
                val list= arrayListOf(
                    carCompanies
                )
                binding.recyclerViewChip.adapter=CarRentSelectedItems(list)
            }

            load()

        }

        getBackStackData<Content>("onCarModelsSelected", true) {
            carModels=it

            if (countryContent.id != -10) {
                binding.countryName.text = countryContent.name
            }
            if (currencyContent.id != -10) {
                binding.currency.text = currencyContent.name
            }

           if ( carCompanies.id!=-10){
               val list= arrayListOf(
                   carCompanies,
                   carModels
               )
               binding.recyclerViewChip.adapter=CarRentSelectedItems(list)
           }else{
               val list= arrayListOf(
                   carModels
               )
               binding.recyclerViewChip.adapter=CarRentSelectedItems(list)
           }

            load()

        }

    }

    companion object{
        private var carCompanies = Content(-10, "")
        private var carModels = Content(-10, "")

        private var countryContent = Content(-10, "")
        private var currencyContent = Content(-10, "")
    }

    private fun direct(idNum:Int){
        val action =
            FragmentCarRentCarDirections.actionFragmentCarRentCarToFragmentTourismFIlter(
                idNum
            )
        findNavController().navigate(action)
    }


    private fun load() {
        Common.carRentPageId = 1
        viewModel.startCarViewModel(headerMapUniversal(requireContext()), "", countryContent.id, currencyContent.id, "", mapKeys, args.carId)
    }

    private fun adapterSet() {

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
        })


        val arrayList: MutableSet<Data> = HashSet()
        val arrayList2: MutableSet<Data> = HashSet()
        viewModel.carRentCarObserver.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            arrayList2.addAll(it.content.data)

            for (i in it.content.data.indices) {
                arrayList.add(it.content.data[i])
            }
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
            binding.counter.text = arrayList2.size.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun carSelected(position: Data) {
        val action=FragmentCarRentCarDirections.actionFragmentCarRentCarToFragmentCarView(position.id)
        findNavController().navigate(action)
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