package com.tesseract.AllOneClient.fragments.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentSearchCancelledBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSearchCancelled: Fragment(R.layout.fragment_search_cancelled) {

    private var _binding: FragmentSearchCancelledBinding?=null
    private val binding get() = _binding!!
    private val args: FragmentSearchCancelledArgs by navArgs()

    private val shareViewModel: ShareDataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSearchCancelledBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        if (args.isFromRegion){
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                loadItems(it)
            })
        }else{
            shareViewModel.parcelItem.observe(viewLifecycleOwner, {

            })
        }

    }

//    @SuppressLint("SetTextI18n")
//    private fun loadItems(it: com.tesseract.AllOneClient.model.parcel.parcelSearch.Content){
//        binding.apply {
//            reusable.found.text=it.found
//            tariff.text=it.order.tariff
//            orderId.text="${requireContext().getString(R.string.orderrr)} ${it.order.id}"
//            price.text= SaveData.formatPhone(it.order.price)+requireContext().getString(R.string.summa1)
//        }
//    }


    @SuppressLint("SetTextI18n")
    private fun loadItems(it: Content){
        binding.apply {
            tariff.text=it.order.tariff
            places.text=it.order.places
            price.text= SaveData.formatPhone(it.order.price)+requireContext().getString(R.string.summa1)

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}