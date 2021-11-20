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
import com.tesseract.AllOneClient.fragments.medTurism.clinics.FragmentClinicsDirections
import com.tesseract.AllOneClient.model.tourism.indexUzb.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_get_bonus.*

@AndroidEntryPoint
class FragmentTourismPackages : Fragment(), TourPackagesAdapter.OnExploreListener {
    private var _binding: FragmentTourismPackagesBinding? = null

    val args:FragmentTourismPackagesArgs by navArgs()
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

        Common.tourIndexMain = 1
        if (args.location=="uzbekistan"){
            viewModel.startMainIndex(headerMapUniversal(requireContext()), args.location, "", args.uzbId, 1, "by_popularity")
        }
        viewModel.startMainIndex(headerMapUniversal(requireContext()), args.location, "", 1, 1, "by_popularity")
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
                    viewModel.mainIndex(headerMapUniversal(requireContext()), args.location, "", 1, 1, "")
                }
            })
        }
        adapterSet()
    }

    private fun adapterSet() {

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        val arrayList: MutableSet<Data> = HashSet()
        viewModel.mainIndex.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            for (i in it.indices) {
                arrayList.add(it[i])
            }
            if (arrayList.size != 0) {
                adapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onExploreListener(position: Data) {
        val action=FragmentTourismPackagesDirections.actionFragmentTourismPackagesToFragmentPackagesView(position.id)
        findNavController().navigate(action)
    }
}