package com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView

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
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.clinics.PagerAdapter
import com.tesseract.AllOneClient.adapter.tourism.hotel.HotelPagerAdapter
import com.tesseract.AllOneClient.databinding.FragmentHotelViewBinding
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_order_taxi.*
import java.lang.Exception

@AndroidEntryPoint
class FragmentHotelView :Fragment() {

    private var _binding:FragmentHotelViewBinding?=null
    private val binding get() = _binding!!
    private val args: FragmentHotelViewArgs by navArgs()

    private lateinit var viewModel: HotelViewModel
    private val shareViewModel: HotelViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHotelViewBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(HotelViewModel::class.java)
        return binding.root
    }

    companion object{
        var hotelId:Int=-10
    }
    private var tourismOrMed=""
    private var hotelOrSan=""

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.fromWhichLayout==0){
            Common.fromWhichLayout =0
            tourismOrMed="tourism"
            hotelOrSan="hotel"
        }else{
            Common.fromWhichLayout =1
            tourismOrMed="med_tourism"
            hotelOrSan="sanatorium"
        }

        var isFavourite=false
        viewModel.hotelView(headerMapUniversal(requireContext()), tourismOrMed,hotelOrSan, args.hotelId)

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
        })

        hotelId=args.hotelId

        viewModel.hotelModel.observe(viewLifecycleOwner, {
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

            binding.call.setOnClickListener { view->
                if (it.content.phone_number.isNullOrEmpty()){
                    Toast.makeText(context, "Phone number not found", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                gotoContact(it.content.phone_number[0], requireContext())
            }

            binding.apply {
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.tabGravity = TabLayout.GRAVITY_FILL

                val adapter = HotelPagerAdapter(childFragmentManager, tabLayout.tabCount, requireContext())
                viewPager2.adapter = adapter
                tabLayout.setupWithViewPager(viewPager2)
            }
        })

        viewModel.hotelError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.save.setOnClickListener { someId->
            try {
                if (!isFavourite){
                    viewModel.hotelAddToFav(headerMapUniversal(requireContext()),tourismOrMed,hotelOrSan,args.hotelId)
                    binding.loader.loader.visibility=View.VISIBLE
                }
            }catch (e: Exception){

            }
        }

        viewModel.hotelAddToFav.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility=View.GONE
            isFavourite=true
        })

    }


}