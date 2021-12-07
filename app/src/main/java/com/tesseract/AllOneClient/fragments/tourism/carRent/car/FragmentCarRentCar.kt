package com.tesseract.AllOneClient.fragments.tourism.carRent.car

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.carRent.CarRentCarsAdapter
import com.tesseract.AllOneClient.databinding.FragmentCarRentCarsBinding
import com.tesseract.AllOneClient.fragments.tourism.mainTourism.FragmentTourismMain
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
    private var isFirst:Boolean=true
    private var isFirstView:Boolean=true
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
        if (sorting.isNotEmpty()){
            binding.bySorting.text=getSorting(sorting)
        }

        Common.carRentPageId=1
        if (isFirst){
            countryContent.id=FragmentTourismMain.world_default_currency_id
            sorting=FragmentTourismMain.default_sort
            currencyContent.id=FragmentTourismMain.uzb_default_currency_id
            viewModel.startCarViewModel(
                headerMapUniversal(requireContext()),
                queryTextChange,
                countryContent.id,
                currencyContent.id,
                sorting,
                args.carId)
        }
        if (!isFirstView&&!isFirst){
            viewModel.startCarViewModel(
                headerMapUniversal(requireContext()),
                queryTextChange,
                countryContent.id,
                currencyContent.id,
                sorting,
                args.carId)
        }


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
                    viewModel.carRentRequest(headerMapUniversal(requireContext()), queryTextChange, countryContent.id, currencyContent.id, sorting,  args.carId)
                }
            })

        }
        adapterSet()
        searchViewListener()
        binding.apply {
            bySorting.setOnClickListener {
                direct(1)
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

        getBackStackData<String>("onSortClickKey", true) {
            sorting=it
            restoreSavedView()
            load()
        }

        getBackStackData<Content>("onCountrySelectKey", true) {
            countryContent = it
            restoreSavedView()
            load()
        }

        getBackStackData<Content>("onCurrencySelectedKey", true) {

            currencyContent = it
            restoreSavedView()
            load()
        }

    }

    companion object{
        private var countryContent = Content(-10, "")
        private var currencyContent = Content(-10, "")
    }

    private fun direct(idNum:Int){
        val action =
            FragmentCarRentCarDirections.actionFragmentCarRentCarToFragmentTourismFIlter(
                idNum
            )
        isFirst=false
        findNavController().navigate(action)
    }


    private fun load() {
        Common.carRentPageId = 1
        viewModel.startCarViewModel(headerMapUniversal(requireContext()), queryTextChange, countryContent.id, currencyContent.id, sorting,  args.carId)
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
        isFirstView=false
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

    private fun getSorting(sortKey:String):String{
        return  when (sortKey) {
            "by_popularity" -> getString(R.string.by_popularity)
            "newest" -> getString(R.string.newest)
            "alphabetically" -> getString(R.string.alphabetically)
            "ascending_price" -> getString(R.string.ascending_price)
            else -> getString(R.string.descending_price)
        }
    }

    private var sorting :String=""
    private fun restoreSavedView(){
        if (currencyContent.name.isNotEmpty()){
            binding.currency.text = currencyContent.name
        }
        if (countryContent.name.isNotEmpty()) {
            binding.countryName.text = countryContent.name
        }
        if (sorting.isNotEmpty()) {
            binding.bySorting.text = getSorting(sorting)
        }
    }
    private var queryTextChange:String=""
    private fun searchViewListener(){
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    queryTextChange=newText!!
                    Toast.makeText(context, queryTextChange, Toast.LENGTH_SHORT).show()
                    viewModel.carRentRequest(headerMapUniversal(requireContext()),
                        queryTextChange,
                        countryContent.id,
                        currencyContent.id,
                        sorting,  args.carId)
                    return false
                }

            })
        }
    }




}