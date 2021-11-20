package com.tesseract.AllOneClient.fragments.tourism.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.adapter.tourism.ExplorePageAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourExploreBinding
import com.tesseract.AllOneClient.fragments.tourism.mainTourism.FragmentTourismMainDirections
import com.tesseract.AllOneClient.model.tourism.explore.Content
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentTourExplore:Fragment() , ExplorePageAdapter.OnExploreListener{
    private var _binding:FragmentTourExploreBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: ExplorePageAdapter
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private lateinit var viewModel: ExploreViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentTourExploreBinding.inflate(inflater, container, false)

        viewModel=ViewModelProvider(this).get(ExploreViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.explore(headerMapUniversal(requireContext()))

        viewModel.errorM.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.exploreObserver.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            binding.recyclerViewDiscover.layoutManager=GridLayoutManager(context, 2)
            adapter=ExplorePageAdapter(this, it.content)
            binding.recyclerViewDiscover.adapter=adapter
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onExploreListener(position: Content) {
        val action= FragmentTourExploreDirections.actionGlobalTourismExplore(position.id)
        findNavController().navigate(action)
    }


}