package com.tesseract.AllOneClient.fragments.medTurism.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.adapter.medTourism.FavouritesAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedTurFavouriteBinding
import com.tesseract.AllOneClient.model.medTourism.favourites.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentMedTurFavourite:Fragment(),FavouritesAdapter.OnClickListener, FavouritesAdapter.OnRemoveListener {

    private lateinit var viewModel: MedTurFavouriteViewModel
    private  var _binding:FragmentMedTurFavouriteBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter:FavouritesAdapter
    private val args:FragmentMedTurFavouriteArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMedTurFavouriteBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(MedTurFavouriteViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.favouritesPagingApi = 1
        viewModel.starterFun(headerMapUniversal(requireContext()), args.medOrTour)


        adapter = FavouritesAdapter(mutableSetOf(), this, this)


        binding.apply {
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }

            loader.loader.visibility = View.VISIBLE
            val layoutManager2 =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = layoutManager2
            recyclerView.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager2) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.favourites(headerMapUniversal(requireContext()), args.medOrTour)
                }
            })



            val arrayList:MutableSet<Data> =HashSet()
            viewModel.favouritesObserver.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility = View.GONE
                if (it.content.data.isEmpty()){
                }else{
                    binding.linearLayout.visibility=View.GONE
                }
                arrayList.addAll(it.content.data)

                if (arrayList.size != 0) {
                    adapter.addList(arrayList)
                }
                arrayList.clear()
            })

            viewModel.deleteFromFavourites.observe(viewLifecycleOwner, {
                adapter.removedItem(data, position)
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility = View.GONE
            })

            viewModel.errorDelet.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility = View.GONE
            })
            viewModel.errorFAV.observe(viewLifecycleOwner,{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility = View.GONE
            })


        }

    }

    override fun onChipClicked(position: Data) {

    }

    private var position by Delegates.notNull<Int>()
    private lateinit var data: Data
    override fun removeRequest(data: Data, position: Int) {
        binding.loader.loader.visibility = View.VISIBLE

        this.data=data
        this.position=position
        viewModel.delete(headerMapUniversal(requireContext()),args.medOrTour, data.id, data.type)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}