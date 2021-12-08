package com.tesseract.AllOneClient.fragments.main.home.SearchTaxi

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.SearchTaxiAdapter2
import com.tesseract.AllOneClient.adapter.home.SearchTaxisAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentSearchTaxisBinding
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.fragments.profile.techSupport.ContactViewModel
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.model.home.SearchModel.YourRequest
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.gotoTelegram
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.reuse_search_for_order_bottom.*

@AndroidEntryPoint
class FragmentSearchTaxi : Fragment(),
    SearchTaxisAdapter.OnItemClickListener, SearchTaxiAdapter2.OnItemClickListener {
    private var _binding: FragmentSearchTaxisBinding? = null
    private val binding get() = _binding!!
    private val shareViewModel: ShareDataViewModel by activityViewModels()
    private lateinit var searchTaxisAdapter: SearchTaxisAdapter
    private lateinit var searchTaxiAdapter2: SearchTaxiAdapter2
    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSearchTaxisBinding.inflate(inflater, container, false)
        viewModel= ViewModelProvider(this).get(ContactViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelListener()

        viewModel.contactError.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.contacts.observe(viewLifecycleOwner, {

            binding.forBottom.telegramAccout.text=it.content.tg_account
            binding.forBottom.operatorNumber.text=it.content.phone_number

            binding.forBottom.telegram.setOnClickListener {view->
                gotoTelegram(it.content.tg_account, requireContext())
            }

            binding.forBottom.call.setOnClickListener {view->
                gotoContact(it.content.phone_number, requireContext())
            }

        })
    }

    private fun viewModelListener(){
        shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
            loadItems(it)
            searchTaxisAdapter = SearchTaxisAdapter(requireContext(), it.your_request as ArrayList<YourRequest>,  this)
            binding.recyclerQuery.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerQuery.adapter = searchTaxisAdapter

            searchTaxiAdapter2 = SearchTaxiAdapter2(requireContext(), it.your_request,  this)
            binding.recyclerViewOptions.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerViewOptions.adapter = searchTaxiAdapter2

        })
    }
    @SuppressLint("SetTextI18n")
    private fun loadItems(it: Content){
        binding.apply {
            topReuse.found.text=it.found
            tariff.text=it.order.tariff
            places.text=it.order.places
            price.text=SaveData.formatPhone(it.order.price)+requireContext().getString(R.string.summa1)

        }
    }

    override fun onItemClick(position: Int) {
        val action =
            FragmentSearchTaxiDirections.actionFragmentSearchTaxiToFragmentRegionDriverInfo( position, true)
        findNavController().navigate(action)
    }

    override fun onOtherOptionsClick(position: Int) {
        val action =
            FragmentSearchTaxiDirections.actionFragmentSearchTaxiToFragmentRegionDriverInfo(position, false)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


}