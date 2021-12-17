package com.tesseract.AllOneClient.fragments.tourism.tourPackages

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.TourPackagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismPackagesBinding
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.model.tourism.indexUzb.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class FragmentTourismPackages : Fragment(), TourPackagesAdapter.OnExploreListener {
    private var _binding: FragmentTourismPackagesBinding? = null

    val args: FragmentTourismPackagesArgs by navArgs()
    private val binding get() = _binding!!

    private lateinit var viewModel: TourPackagesViewModel
    private lateinit var adapter: TourPackagesAdapter
    private var sorting: String = ""
    private var isFirst: Boolean = true
    private var isFirstView: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTourismPackagesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TourPackagesViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loader.loader.visibility=View.VISIBLE

        if (currencyContent.name.isNotEmpty()) {
            binding.currency.text = currencyContent.name
        }
        if (countryContent.name.isNotEmpty()) {
            binding.countryName.text = countryContent.name
        }
        if (sorting.isNotEmpty()) {
            binding.byPopularity.text = getSorting(sorting)
        }

        if (args.location=="uzbekistan"){
            binding.countryName.visibility=View.GONE
        }

        searchViewListener()
        Common.tourIndexMain = 1
        if (isFirst) {
            if (args.location == "uzbekistan") {
                sorting=args.defaultSort
                countryContent.id=args.uzbId
                currencyContent.id=args.uzbDefaultCurrencyId

                viewModel.startMainIndex(
                    headerMapUniversal(requireContext()),
                    args.location,
                    queryTextChange,
                    args.uzbId,
                    currencyContent.id,
                    sorting
                )
            } else {

                sorting=args.defaultSort
                countryContent.id=args.uzbDefaultCurrencyId
                currencyContent.id=args.worldDefaultCurrencyId

                viewModel.startMainIndex(
                    headerMapUniversal(requireContext()),
                    args.location,
                    queryTextChange,
                    countryContent.id,
                    currencyContent.id,
                    sorting
                )
            }
        }
        if (!isFirst && !isFirstView) {
            if (args.location == "uzbekistan") {
               // binding.countryName.text = getString(R.string.uzbekistan)
                viewModel.startMainIndex(
                    headerMapUniversal(requireContext()),
                    args.location,
                    queryTextChange,
                    args.uzbId,
                    currencyContent.id,
                    sorting
                )
            } else {
                viewModel.startMainIndex(
                    headerMapUniversal(requireContext()),
                    args.location,
                    queryTextChange,
                    countryContent.id,
                    currencyContent.id,
                    sorting
                )
            }

        }

        adapter = TourPackagesAdapter(this, mutableSetOf())


        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            loader.loader.visibility = View.VISIBLE
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager2
            recyclerView.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    if (args.location == "uzbekistan") {
                        viewModel.mainIndex(
                            headerMapUniversal(requireContext()),
                            args.location,
                            queryTextChange,
                            args.uzbId,
                            currencyContent.id,
                            sorting
                        )
                    } else {
                        viewModel.mainIndex(
                            headerMapUniversal(requireContext()),
                            args.location,
                            queryTextChange,
                            countryContent.id,
                            currencyContent.id,
                            sorting
                        )
                    }
                }
            })

            byPopularity.setOnClickListener {
                val action = FragmentTourismPackagesDirections.actionGlobalTourismFilter2(1)
                isFirst = false
                findNavController().navigate(action)
            }


            countryName.setOnClickListener {
                val action = FragmentTourismPackagesDirections.actionGlobalTourismFilter2(2)
                isFirst = false
                findNavController().navigate(action)
            }
            currency.setOnClickListener {
                val action = FragmentTourismPackagesDirections.actionGlobalTourismFilter2(3)
                isFirst = false
                findNavController().navigate(action)
            }
        }
        adapterSet()
        getBackStackData<Int>("cancelledInfo", true) {
            load()
        }

        getBackStackData<String>("onSortClickKey", true) {
            sorting = it
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

    private fun load() {
        Common.tourIndexMain = 1
        viewModel.startMainIndex(
            headerMapUniversal(requireContext()),
            args.location,
            queryTextChange,
            countryContent.id,
            currencyContent.id,
            sorting
        )
    }


    private var countryContent = Content(-10, "")
    private var currencyContent = Content(-10, "")

    private fun adapterSet() {

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        val arrayList: MutableSet<Data> = HashSet()
        val arrayList2: MutableSet<Data> = HashSet()
        viewModel.mainIndex.observe(viewLifecycleOwner, {
            try {
                binding.loader.loader.visibility = View.GONE
                arrayList2.addAll(it)
                for (i in it.indices) {
                    arrayList.add(it[i])
                }
                if (arrayList.size != 0) {
                    adapter.addList(arrayList)
                }
                arrayList.clear()
                binding.count.text = arrayList2.size.toString()
            }catch (e:Exception){
                Log.i(TAG, "adapterSet: ${e.message}")
            }


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onExploreListener(position: Data) {
        val action =
            FragmentTourismPackagesDirections.actionGlobalTourismFilter(position.id)
        isFirstView = false
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

    private fun restoreSavedView(){
        if (currencyContent.name.isNotEmpty()){
            binding.currency.text = currencyContent.name
        }
        if (countryContent.name.isNotEmpty()) {
            binding.countryName.text = countryContent.name
        }
        if (sorting.isNotEmpty()) {
            binding.byPopularity.text = getSorting(sorting)
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
                    viewModel.startMainIndex(
                        headerMapUniversal(requireContext()),
                        args.location,
                        queryTextChange,
                        countryContent.id,
                        currencyContent.id,
                        sorting
                    )
                    return true
                }

            })
        }
    }
}