package com.tesseract.AllOneClient.fragments.tourism.tourPackets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.TourPacketAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourismTourPacketBinding
import com.tesseract.AllOneClient.model.tourism.TourPacketsModel

class FragmentTourPackets : Fragment() , TourPacketAdapter.OnImageClickListener{
    private var binding: FragmentTourismTourPacketBinding?=null
    private lateinit var tourPacketsModel: List<TourPacketsModel>
    private lateinit var tourPacketAdapter: TourPacketAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTourismTourPacketBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadItems()

        binding?.recyclerView?.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        tourPacketAdapter= TourPacketAdapter(tourPacketsModel, this)
        binding?.recyclerView?.adapter=tourPacketAdapter
        binding?.recyclerView?.setHasFixedSize(true)


    }

    fun loadItems(){
        tourPacketsModel= listOf(
            TourPacketsModel(
                R.drawable.blue_sky,
                "Тур Горы Ташкента 2021 однодневная экскурсия Чимган и Чарвак",
                "С вершины Чимганских гор открывается величественная панорама!",
            "от 10\$/человека",
                "1 день"
            ),
            TourPacketsModel(
                R.drawable.two_girls,
                "Тур в Узбекистан из Нурсултана \"Восточная сказка\"",
                "Отправляйтесь в тур в Узбекистан из Нурсултана, чтобы проникнуться волшебной сказкой древней Восточной страны!",
                "от 59\$/человека",
                "4-5 дня"
            ),
            TourPacketsModel(
                R.drawable.one_girl,
                "Сборный гарантированный тур в Узбекистан на Майские Праздники из Москвы",
                "Сборный тур в Узбекистан на майские праздники 2021 - 4 заезда. Москва и Санкт-Петербург - встречайте весну.",
                "от 19\$/человека",
                "4 дня"
            ),
        )
    }

    override fun onItemClick(position: Int) {
        val action=FragmentTourPacketsDirections.actionFragmentTourPacketsToFragmentTour()
        findNavController().navigate(action)
    }
}