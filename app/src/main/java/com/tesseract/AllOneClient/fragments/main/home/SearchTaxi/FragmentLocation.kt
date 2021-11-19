package com.tesseract.AllOneClient.fragments.main.home.SearchTaxi

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentLocationBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentLocation : Fragment(R.layout.fragment_location) {
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding!!
    private val args:FragmentLocationArgs by navArgs()

    private lateinit var viewModel: SearchTaxiViewModel
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=ViewModelProvider(this).get(SearchTaxiViewModel::class.java)
        val contextThemeWrapper: Context = ContextThemeWrapper(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        return localInflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val locationFragmentBinging = FragmentLocationBinding.bind(view)
        _binding = locationFragmentBinging
        requireActivity().statusBarColor(
                    ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
        ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
        false
        )
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        if (args.fromWhichLayout==0){
            viewModel.parcelSearchRequest(headerMapUniversal(requireContext()), args.orderId)
            viewModel.parcelSearchModel.observe(viewLifecycleOwner, {
                shareViewModel.parcelSearchItem(it)
                val action= FragmentLocationDirections.actionFragmentLocationToFragmentParcelSearch(args.orderId)
                findNavController().navigate(action)
            })
        }else if (args.fromWhichLayout==1){
            viewModel.searchRegionTaxiOrder(headerMapUniversal(requireContext()),args.orderId)
            viewModel.searchTaxiResponse.observe(viewLifecycleOwner, {
                shareViewModel.searchOrder(it)
                val action= FragmentLocationDirections.actionFragmentLocationToFragmentSearchTaxi2()
                findNavController().navigate(action)
            })
        }


    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}