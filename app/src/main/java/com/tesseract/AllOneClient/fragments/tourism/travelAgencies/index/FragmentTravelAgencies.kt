package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.index

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
import com.tesseract.AllOneClient.adapter.tourism.travelAgency.TravelAgencyIndexAdapter
import com.tesseract.AllOneClient.databinding.FragmentTravelAgenciesBinding
import com.tesseract.AllOneClient.fragments.tourism.hotels.index.FragmentHotelIndexArgs
import com.tesseract.AllOneClient.fragments.tourism.tourPackages.FragmentTourismPackagesDirections
import com.tesseract.AllOneClient.model.tourism.countries.Content
import com.tesseract.AllOneClient.model.tourism.agency.index.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTravelAgencies : Fragment() ,TravelAgencyIndexAdapter.OnChipClickListener{
    private var _binding: FragmentTravelAgenciesBinding? = null
    private val binding get() =  _binding!!
    private lateinit var adapter: TravelAgencyIndexAdapter
    private lateinit var viewModel: TravelAgencyViewModel
    private val args: FragmentTravelAgenciesArgs by navArgs()


    private var sorting:String=""
    private var isFirst:Boolean=true
    private var isFirstView:Boolean=true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTravelAgenciesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(TravelAgencyViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.travelPagerId = 1

        if (countryContent.name.isNotEmpty()){
            binding.countryName.text=countryContent.name
        }
        if (sorting.isNotEmpty()){
            binding.byPopularity.text=sorting
        }



        if (isFirst){
            sorting=args.defaultSort
            countryContent.id=args.uzbId
            viewModel.startAgencyIndex(headerMapUniversal(requireContext()), "", countryContent.id, sorting)
        }

        if (!isFirstView&&!isFirst){
            viewModel.startAgencyIndex(headerMapUniversal(requireContext()), "", countryContent.id, sorting)
        }



        adapter=TravelAgencyIndexAdapter(this, mutableSetOf())
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
                    viewModel.agencyIndex(
                        headerMapUniversal(requireContext()),
                        "",
                        countryContent.id,
                        sorting
                    )
                }
            })

            byPopularity.setOnClickListener {
                val action =
                    FragmentTourismPackagesDirections.actionGlobalTourismFilter2(1)
                isFirst=false
                findNavController().navigate(action)
            }

            countryName.setOnClickListener {
                val action =
                    FragmentTourismPackagesDirections.actionGlobalTourismFilter2(2)
                isFirst=false
                findNavController().navigate(action)
            }
        }

        adapterSet()

        getBackStackData<Int>("cancelledInfo", true) {
            load()
        }

        getBackStackData<String>("onSortClickKey", true) {
            binding.byPopularity.text=it
            sorting=it

            if (countryContent.name.isNotEmpty()) {
                binding.countryName.text = countryContent.name
            }

            load()
        }


        getBackStackData<Content>("onCountrySelectKey", true) {
            binding.countryName.text = it.name
            countryContent = it
            if (sorting.isNotEmpty()){
                binding.byPopularity.text=sorting
            }

            load()
        }
    }

    private fun load() {
        Common.travelPagerId = 1
        viewModel.agencyIndex(
            headerMapUniversal(requireContext()),
            "",
            countryContent.id,
            sorting
        )
    }

    private var countryContent = Content(-10, "")

    private fun adapterSet() {

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        val arrayList: MutableSet<Data> = HashSet()
        val arrayList2: MutableSet<Data> = HashSet()
        viewModel.agencyIndex.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            arrayList2.addAll(it.content.data)

            arrayList.addAll(it.content.data)
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
            binding.counter.text = arrayList2.size.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun onChipClicked(position: Data) {
        val action=FragmentTravelAgenciesDirections.actionFragmentTravelAgenciesToFragmentAgencyView(position.id)
        isFirstView=false
        findNavController().navigate(action)
    }

}