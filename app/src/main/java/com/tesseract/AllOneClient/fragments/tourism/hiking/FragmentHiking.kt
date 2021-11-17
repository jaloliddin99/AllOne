package com.tesseract.AllOneClient.fragments.tourism.hiking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.tourism.HikingAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismHikingBinding
import com.tesseract.AllOneClient.model.tourism.HikingModel

class FragmentHiking: Fragment(), HikingAdapter.OnItemClicked {

    private var binding: FragmentTourismHikingBinding?=null

    private lateinit var hikingModel: List<HikingModel>
    private lateinit var hikingAdapter: HikingAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTourismHikingBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadItems()

        binding?.recyclerView?.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        hikingAdapter= HikingAdapter(requireContext(), hikingModel, this)
        binding?.recyclerView?.adapter=hikingAdapter
        binding?.recyclerView?.setHasFixedSize(true)

    }

    fun loadItems(){
        hikingModel= listOf(
            HikingModel("Жемчужина Азии", "Место: Кайраташ", "Кол-во человек: 12", "Дата: 23.05.2021", "Траснпорт: Miniven", "Цена: 230 000 сум/человек"),
            HikingModel("Жемчужина Азии", "Место: Кайраташ", "Кол-во человек: 12", "Дата: 23.05.2021", "Траснпорт: Miniven", "Цена: 230 000 сум/человек"),
            HikingModel("Жемчужина Азии", "Место: Кайраташ", "Кол-во человек: 12", "Дата: 23.05.2021", "Траснпорт: Miniven", "Цена: 230 000 сум/человек"),
            HikingModel("Жемчужина Азии", "Место: Кайраташ", "Кол-во человек: 12", "Дата: 23.05.2021", "Траснпорт: Miniven", "Цена: 230 000 сум/человек"),
            )
    }

    override fun onItemClick(position: Int) {
        val action=FragmentHikingDirections.actionFragmentHikingToFragmentActiveTour()
        findNavController().navigate(action)
    }

}