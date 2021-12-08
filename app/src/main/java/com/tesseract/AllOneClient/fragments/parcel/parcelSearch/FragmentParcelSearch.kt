package com.tesseract.AllOneClient.fragments.parcel.parcelSearch

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.postService.ParcelSearchFoundBottomAdapter
import com.tesseract.AllOneClient.adapter.postService.ParcelSearchFoundTopAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentParcelSearchBinding
import com.tesseract.AllOneClient.fragments.main.home.SearchTaxi.SearchTaxiViewModel
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.profile.techSupport.ContactViewModel
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentParcelSearch:Fragment(),ParcelSearchFoundTopAdapter.OnItemClickListener,
    ParcelSearchFoundBottomAdapter.OnItemClickListener{
    private lateinit var binding:FragmentParcelSearchBinding
    private lateinit var viewModel: SearchTaxiViewModel
    private lateinit var topAdapter:ParcelSearchFoundTopAdapter
    private lateinit var bottomAdapter:ParcelSearchFoundBottomAdapter
    private lateinit var viewModel2: ContactViewModel

    private val args:FragmentParcelSearchArgs by navArgs()

    private val shareViewModel: ShareDataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParcelSearchBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(SearchTaxiViewModel::class.java)
        viewModel2= ViewModelProvider(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelListener()

        viewModel2.contactError.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel2.contacts.observe(viewLifecycleOwner, {

            binding.bottomReuse.telegramAccout.text=it.content.tg_account
            binding.bottomReuse.operatorNumber.text=it.content.phone_number

            binding.bottomReuse.telegram.setOnClickListener {view->
                gotoTelegram(it.content.tg_account, requireContext())
            }

            binding.bottomReuse.call.setOnClickListener {view->
                gotoContact(it.content.phone_number, requireContext())
            }

        })

    }

    private fun viewModelListener(){
        shareViewModel.parcelItem.observe(viewLifecycleOwner, {
            loadItems(it)
            topAdapter = ParcelSearchFoundTopAdapter(requireContext(), it.your_request ,  this)
            binding.recyclerQuery.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerQuery.adapter = topAdapter

            bottomAdapter = ParcelSearchFoundBottomAdapter(requireContext(), it.other_options,  this)
            binding.recyclerViewOptions.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerViewOptions.adapter = bottomAdapter
        })

    }
    @SuppressLint("SetTextI18n")
    private fun loadItems(it: com.tesseract.AllOneClient.model.parcel.parcelSearch.Content){
        binding.apply {
            reusable.found.text=it.found
            tariff.text=it.order.tariff
            orderId.text="${requireContext().getString(R.string.orderrr)} ${it.order.id}"
            price.text= SaveData.formatPhone(it.order.price)+requireContext().getString(R.string.summa1)


        }
    }

    override fun onYourRequestOptionsClick(position: Int) {
        Log.i(TAG, "onYourRequestOptionsClick: ${args.parcelOrderId}")
        val action =
            FragmentParcelSearchDirections.actionFragmentParcelSearchToFragmentParcelSelectedItem( true,position,  args.parcelOrderId)
        findNavController().navigate(action)
    }

    override fun onOtherOptionsClick(position: Int) {
        Log.i(TAG, "onYourRequestOptionsClick: ${args.parcelOrderId}")
        val action =
            FragmentParcelSearchDirections.actionFragmentParcelSearchToFragmentParcelSelectedItem( false,position,  args.parcelOrderId)
        findNavController().navigate(action)
    }
}