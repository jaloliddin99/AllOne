package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.hotel.HotelPagerAdapter
import com.tesseract.AllOneClient.adapter.tourism.travelAgency.AgencyPagerAdapter
import com.tesseract.AllOneClient.databinding.FragmentAgencyViewBinding
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.FragmentHotelView
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.HotelViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentAgencyView:Fragment() {
    private var _binding:FragmentAgencyViewBinding?=null
    private val args:FragmentAgencyViewArgs by navArgs()

    companion object{
        var agencyId by Delegates.notNull<Int>()
    }

    private val binding get() = _binding!!
    private lateinit var viewModel: AgencyViewModel
    private val shareViewModel: AgencyViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAgencyViewBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(AgencyViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        agencyId=args.agencyId

        var isFavourite=false
        viewModel.agencyView(headerMapUniversal(requireContext()), args.agencyId)

        viewModel.errorAgency.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
        })


        viewModel.agencyView.observe(viewLifecycleOwner, {
            binding.backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            shareViewModel.clinicInfo(it)
            binding.loader.loader.visibility=View.GONE
            isFavourite=it.content.is_favorite
            if (it.content.is_favorite){
                binding.save.setImageResource(R.drawable.ic_saved)
            }else{
                binding.save.setImageResource(R.drawable.ic_savee)
            }

            binding.name.text=it.content.name
            Picasso.get().load(it.content.poster).into(binding.poster)
            binding.rating.text=it.content.rating
            binding.rateCount.text="(${it.content.rate_count})"

            binding.apply {
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.tabGravity = TabLayout.GRAVITY_FILL

                val adapter = AgencyPagerAdapter(childFragmentManager, tabLayout.tabCount, requireContext())
                viewPager2.adapter = adapter
                tabLayout.setupWithViewPager(viewPager2)
            }
        })

        viewModel.errorAgency.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.save.setOnClickListener { someId->
            try {
                if (!isFavourite){
                    viewModel.agencyRateView(headerMapUniversal(requireContext()),args.agencyId)
                    binding.loader.loader.visibility=View.VISIBLE
                }
            }catch (e: Exception){

            }
        }

        viewModel.agencyRateView.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility=View.GONE
            isFavourite=true
        })



    }


}