package com.tesseract.AllOneClient.fragments.charity.MakeDonation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.charity.MakeDonationAdapter
import com.tesseract.AllOneClient.databinding.FragmentMakeDonationBinding
import com.tesseract.AllOneClient.model.charity.projects.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FragmentMakeDonation : Fragment(), MakeDonationAdapter.OnItemCLicked {

    var _binding:FragmentMakeDonationBinding?=null
    val binding get() = _binding!!
    private lateinit var makeDonationAdapter: MakeDonationAdapter
    private var isCurrentFragment: Boolean = true
    private lateinit var viewModel: ProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMakeDonationBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(ProjectViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        Common.donationProjects = 1
        viewModel.startMain(headerMapUniversal(requireContext()))

        binding.apply {
            val layoutManager=GridLayoutManager(context, 2)

            recyclerView.layoutManager=layoutManager
            makeDonationAdapter=MakeDonationAdapter(this@FragmentMakeDonation, mutableSetOf())
            recyclerView.adapter=makeDonationAdapter

            recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.getData(headerMapUniversal(requireContext()))
                }

            })
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }

            orderHistory()


    }

    private fun orderHistory() {
        val arrayList:MutableSet<Data> = HashSet()
        viewModel.data.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            arrayList.addAll(it)
            if (arrayList.size!=0){
                makeDonationAdapter.addList(arrayList)
            }
            arrayList.clear()
        })

    }

    override fun onItemCLicked(position: Int) {
        val action=FragmentMakeDonationDirections.actionFragmentMakeDonationToFragmentPaymentCardSelection(position)
        isCurrentFragment=false
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}