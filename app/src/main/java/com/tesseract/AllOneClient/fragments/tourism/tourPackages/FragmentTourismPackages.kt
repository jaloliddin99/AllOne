package com.tesseract.AllOneClient.fragments.tourism.tourPackages

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
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.TourPackagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismPackagesBinding
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.model.tourism.indexUzb.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTourismPackages : Fragment(), TourPackagesAdapter.OnExploreListener {
    private var _binding: FragmentTourismPackagesBinding? = null

    val args: FragmentTourismPackagesArgs by navArgs()
    private val binding get() = _binding!!

    private lateinit var viewModel: TourPackagesViewModel
    private lateinit var adapter: TourPackagesAdapter


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


        if (currencyContent.id!=-10){
            binding.currency.text=currencyContent.name
        }
        if (countryContent.id!=-10){
            binding.countryName.text=countryContent.name
        }




        Common.tourIndexMain = 1
        if (args.location == "uzbekistan") {
            viewModel.startMainIndex(
                headerMapUniversal(requireContext()),
                args.location,
                "",
                args.uzbId,
                currencyContent.id,
                "by_popularity"
            )
        } else {
            viewModel.startMainIndex(
                headerMapUniversal(requireContext()),
                args.location,
                "",
                countryContent.id,
                currencyContent.id,
                "by_popularity"
            )
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
                            "",
                            args.uzbId,
                            currencyContent.id,
                            "by_popularity"
                        )
                    } else {
                        viewModel.mainIndex(
                            headerMapUniversal(requireContext()),
                            args.location,
                            "",
                            countryContent.id,
                            currencyContent.id,
                            "by_popularity"
                        )
                    }
                }
            })


            countryName.setOnClickListener {
                val action =
                    FragmentTourismPackagesDirections.actionFragmentTourismPackagesToFragmentTourismFIlter(
                        2
                    )
                findNavController().navigate(action)
            }
            currency.setOnClickListener {
                val action =
                    FragmentTourismPackagesDirections.actionFragmentTourismPackagesToFragmentTourismFIlter(
                        3
                    )
                findNavController().navigate(action)
            }
        }
        adapterSet()

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
    }

    private fun load() {
        Common.tourIndexMain = 1
        if (args.location == "uzbekistan") {
            viewModel.startMainIndex(
                headerMapUniversal(requireContext()),
                args.location,
                "",
                args.uzbId,
                currencyContent.id,
                "by_popularity"
            )
        } else {
            viewModel.startMainIndex(
                headerMapUniversal(requireContext()),
                args.location,
                "",
                countryContent.id,
                currencyContent.id,
                "by_popularity"
            )
        }
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


        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onExploreListener(position: Data) {
        val action =
            FragmentTourismPackagesDirections.actionFragmentTourismPackagesToFragmentPackagesView(
                position.id
            )
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