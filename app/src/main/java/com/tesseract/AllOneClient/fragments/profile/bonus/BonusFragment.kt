package com.tesseract.AllOneClient.fragments.profile.bonus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.profile.BonusItemAdapter
import com.tesseract.AllOneClient.databinding.FragmentBonusBinding
import com.tesseract.AllOneClient.model.profile.bonus.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BonusFragment: Fragment(R.layout.fragment_bonus) {

    private var _binding: FragmentBonusBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel:BonusViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter:BonusItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentBonusBinding.inflate(inflater, container, false)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            true
        )
        viewModel=ViewModelProvider(this).get(BonusViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.howToGetBonus.setOnClickListener {
            val action=BonusFragmentDirections.actionBonusFragment2ToFragmentGetBonusText()
            findNavController().navigate(action)
        }
        Common.getAllPonusesPage=1

        viewModel.startBonus(headerMapUniversal(requireContext()))


        layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            recyclerView.layoutManager = layoutManager
            adapter = BonusItemAdapter(mutableSetOf(), requireContext())
            recyclerView.adapter = adapter

            recyclerView.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.getAllBonuses(headerMapUniversal(requireContext()))
                }

            })
        }

        orderHistory()

    }

    private fun orderHistory() {
        val arrayList:MutableSet<Data> = HashSet()
        viewModel.getAllBonuses.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            arrayList.addAll(it)
            if (arrayList.size!=0){
                adapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}