package com.tesseract.AllOneClient.fragments.tourism.packagesView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.ExtraPaidAdapter
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.PackageTextItem
import com.tesseract.AllOneClient.databinding.FragmentPackageDescriptionBinding
import com.tesseract.AllOneClient.fragments.tourism.packagesView.PackageViewModel
import com.tesseract.AllOneClient.model.tourism.packageView.Include

class FragmentPackageItemDescription:Fragment(), PackageTextItem.OnLocationClickListener {

    private var _binding:FragmentPackageDescriptionBinding?=null
    private val binding get() = _binding!!
    private val shareViewModel: PackageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPackageDescriptionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {

            binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerView.adapter=PackageTextItem(it.content.includes, this)

            binding.recyclerViewExtraPaid.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            binding.recyclerViewExtraPaid.adapter= ExtraPaidAdapter(it.content.extra_paid)
        })


    }

    override fun onItemClick(type: Include?) {

    }

}