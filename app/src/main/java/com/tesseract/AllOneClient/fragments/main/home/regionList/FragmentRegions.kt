package com.tesseract.AllOneClient.fragments.main.home.regionList

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.RegionRegionAdapter
import com.tesseract.AllOneClient.databinding.FragmentRegionRegionBinding
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import com.tesseract.AllOneClient.model.home.getRegions.RegionPopbackStask
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentRegions: Fragment(), RegionRegionAdapter.OnItemClickListener {

    private var _binding:FragmentRegionRegionBinding?=null
    private val binding get() = _binding!!
    private lateinit var regionAdapter: RegionRegionAdapter
    private lateinit var viewModel: RegionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding= FragmentRegionRegionBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(RegionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Common.isCurrentRegionFragment){
            viewModel.getRegionList(headerMapUniversal(requireContext()))
        }
        if (Common.destination == 0||Common.destination== 10) {
            binding.toolbarTitle.text=getString(R.string.wheRee)
        }else{
            binding.toolbarTitle.text=getString(R.string.to_where)
        }

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        binding.searchItemRecycler.layoutManager=
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.regionDetails.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            regionAdapter= RegionRegionAdapter(it, this)
            binding.searchItemRecycler.adapter=regionAdapter
            val resId: Int = R.anim.layout_animation
            val animation = AnimationUtils.loadLayoutAnimation(context, resId)
            binding.searchItemRecycler.layoutAnimation = animation
            binding.searchItemRecycler.setHasFixedSize(true)
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        performSearch()

    }

    override fun onItemClick(
        textView: TextView,
        linearLayout: LinearLayoutCompat,
        position: String,
        regionName: String?,
        searchItemBinding: GetRegionDetails
    ) {
        if (Common.destination == 0||Common.destination== 10) {
            Common.startRegion = regionName!!
            Common.startRegionId = position
        }
        if (Common.destination == 1||Common.destination == 11) {
            Common.endRegion = regionName!!
            Common.endRegionId = position
        }

        if (searchItemBinding.direct==true){


            if (Common.destination == 0||Common.destination== 10) {
                Common.startDistrictId="0"
                Common.startDistrict=""
            }

            if (Common.destination == 1||Common.destination == 11) {
                Common.endDistrictId="0"
                Common.endDistrict=""
            }


            val regionPopbackStask=RegionPopbackStask(
                regionName!!,
                position.toInt(),
                Common.destination
            )
            setBackStackData("regionList", regionPopbackStask, true)
            return
        }

        val direction: NavDirections =
            FragmentRegionsDirections.actionFragmentRegionsToFragmentSearchDistrict()
        val extras = FragmentNavigatorExtras(
            linearLayout to position+"id",
            textView to regionName?.replace(" ", "")+position
        )
        findNavController().navigate(direction, extras)

    }

    private fun performSearch() {
        binding.searchView.addTextChangedListener(textWatcher)

    }
    private val textWatcher=object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            regionAdapter.filter.filter(s)
        }
        override fun afterTextChanged(s: Editable?) {

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