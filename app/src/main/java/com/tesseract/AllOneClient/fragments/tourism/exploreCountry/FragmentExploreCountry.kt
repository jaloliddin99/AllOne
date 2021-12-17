package com.tesseract.AllOneClient.fragments.tourism.exploreCountry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.order.DriverCarImagesAdapter
import com.tesseract.AllOneClient.adapter.tourism.explore.ExploreCPAdapter
import com.tesseract.AllOneClient.databinding.FragmentExploreCountryViewBinding
import com.tesseract.AllOneClient.model.tourism.expCountryPackage.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentExploreCountry:Fragment(), ExploreCPAdapter.OnExploreListener {

    private var _binding:FragmentExploreCountryViewBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: ExploreCountryViewModel
    private lateinit var adapter:ExploreCPAdapter
    private val args : FragmentExploreCountryArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentExploreCountryViewBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(ExploreCountryViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.countryPageee=1
        viewModel.starterExploreCP(headerMapUniversal(requireContext()), args.exploreId)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        adapter= ExploreCPAdapter(this, mutableSetOf())

        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            loader.loader.visibility = View.VISIBLE
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerViewTourPacket.adapter = adapter
            recyclerViewTourPacket.layoutManager = layoutManager2
            recyclerViewTourPacket.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.exploreCP(headerMapUniversal(requireContext()), args.exploreId)
                }
            })

            viewModel.exploreCV(headerMapUniversal(requireContext()), args.exploreId)
            viewModel.exploreCountryView.observe(viewLifecycleOwner, {
                countryName.text=it.content.country_name
                continent.text=it.content.continent
                Picasso.get().load(it.content.poster).into(poster)
                description.text=it.content.description

                recyclerView.adapter=DriverCarImagesAdapter(it.content.gallery)
                recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            })
        }




        adapterSet()
    }

    private fun adapterSet() {

        viewModel.packageError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        val arrayList: MutableSet<Data> = HashSet()
        viewModel.exploreCountryPackages.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            arrayList.addAll(it.content.data)
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onExploreListener(position: Data) {

    }
}