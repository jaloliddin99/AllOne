package com.tesseract.AllOneClient.fragments.main.home.district

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.SearchDistrictAdapter
import com.tesseract.AllOneClient.databinding.FragmentDestrictSearchBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearchDistrict : Fragment(),
    SearchDistrictAdapter.OnItemClick {
    private var _binding: FragmentDestrictSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DistrictViewModel


    private lateinit var searchDistrictAdapter: SearchDistrictAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentDestrictSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DistrictViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            try {
                if (Common.destination == 0 || Common.destination == 10) {
                    toolbarTitle.text = Common.startRegion

                    viewModel.getDistrictDetails(
                        headerMapUniversal(requireContext()),
                        Common.startRegionId
                    )
                }
            }catch (e:Exception){
                println(e.message)
            }


            if (Common.destination == 1 || Common.destination == 11) {
                toolbarTitle.text = Common.endRegion
                viewModel.getDistrictDetails(headerMapUniversal(requireContext()), Common.endRegionId)
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Common.isCurrentRegionFragment = false
                    findNavController().popBackStack()
                }
            })


        binding.backToHome.setOnClickListener {
            Common.isCurrentRegionFragment = false
            findNavController().popBackStack()
        }

        binding.searchItemRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.errorM.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
        })

        viewModel.districtList.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            searchDistrictAdapter = SearchDistrictAdapter(it, this)
            binding.searchItemRecycler.adapter = searchDistrictAdapter
            val resId: Int = R.anim.layout_animation
            val animation = AnimationUtils.loadLayoutAnimation(context, resId)
            binding.searchItemRecycler.layoutAnimation = animation
            binding.searchItemRecycler.setHasFixedSize(true)

        })

        performSearch()



    }

    private fun performSearch() {

        binding.searchView.addTextChangedListener(textWatcher)

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchDistrictAdapter.filter.filter(s)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }


    private var isLocationSelected:Boolean=false
    override fun onItemClick(position: String, districtName: String?) {
        isLocationSelected=true
        ready(districtName, position)
        binding.ready.backgroundTintList = context?.getColorStateList(R.color.green)
    }

    private fun ready(districtName: String?, position: String){
        binding.apply {
            ready.setOnClickListener {
                if (isLocationSelected){
                    if (Common.destination == 0 || Common.destination == 10) {
                        if (districtName != null) {
                            Common.startDistrict = districtName
                            Common.startDistrictId = position

                        }
                    }
                    if (Common.destination == 1 || Common.destination == 11) {
                        if (districtName != null) {
                            Common.endDistrict = districtName
                            Common.endDistrictId = position
                        }
                    }
                    if (Common.destination == 1 || Common.destination == 0) {
                        val action =
                            FragmentSearchDistrictDirections.actionFragmentSearchDistrictToFragmentTaxiRegions()
                        findNavController().navigate(action)
                    }
                    if (Common.destination == 10 || Common.destination == 11) {
                        val action =
                            FragmentSearchDistrictDirections.actionFragmentSearchDistrictToFragmentPostServiceSelection()
                        findNavController().navigate(action)
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

