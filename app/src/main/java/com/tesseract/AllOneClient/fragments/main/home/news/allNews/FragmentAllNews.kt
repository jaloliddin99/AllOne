package com.tesseract.AllOneClient.fragments.main.home.news.allNews

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialElevationScale
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.home.AllNewsAdapter
import com.tesseract.AllOneClient.databinding.FragmentAllNewsBinding
import com.tesseract.AllOneClient.model.home.news.allNews.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllNews:Fragment(), AllNewsAdapter.OnItemClickListener {
    private var isCurrentFragment: Boolean = true
    private lateinit var viewModel: AllNewsViewModel
    var binding:FragmentAllNewsBinding?=null
    private lateinit var allNewsAdapter: AllNewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAllNewsBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(AllNewsViewModel::class.java)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        Common.allNewsCounter=1

        viewModel.start(headerMapUniversal(requireContext()))

        allNewsAdapter= AllNewsAdapter(ArrayList(),this)
        val layoutManager2=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.recyclerView?.layoutManager=layoutManager2
        binding?.recyclerView?.adapter=allNewsAdapter
        binding?.recyclerView?.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager2) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.getAllData(headerMapUniversal(requireContext()))
            }
        })

        binding?.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        loadItems()

    }

    private fun loadItems(){
        val arrayList=ArrayList<Data>()
        viewModel.data.observe(viewLifecycleOwner, {
            Log.i("view is called hi","")
            for (i in it.indices){
                val data=Data(
                    it[i].date,
                    it[i].description,
                    it[i].id,
                    it[i].image,
                    it[i].title
                )
                arrayList.add(data)
            }
            if (arrayList.size!=0){
                allNewsAdapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }


    override fun onItemClick(position: Data, view: MaterialCardView) {
        isCurrentFragment=false
        exitTransition = MaterialElevationScale(false).apply {
            duration = 250.toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 250.toLong()
        }
        val direction: NavDirections =
            FragmentAllNewsDirections.actionFragmentAllNewsToFragmentNewsView(position.image, position.title, position.description, position.id, false)
        val extras = FragmentNavigatorExtras(
            view to "cardViewTransition${position.id}"
        )
        findNavController().navigate(direction, extras)
    }

}