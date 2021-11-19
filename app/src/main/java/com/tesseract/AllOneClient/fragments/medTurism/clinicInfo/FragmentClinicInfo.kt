package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo

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
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.clinics.PagerAdapter
import com.tesseract.AllOneClient.databinding.FragmentClinicInfoBinding
import com.tesseract.AllOneClient.dialogs.medTur.DialogRate
import com.tesseract.AllOneClient.fragments.main.home.payments.ShareDataViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class FragmentClinicInfo:Fragment() {
    private var _binding:FragmentClinicInfoBinding?=null
    private val binding get() = _binding!!
    private val args:FragmentClinicInfoArgs by navArgs()
    private lateinit var viewModel: ClinicsViewModel

    private val shareViewModel: ClinicsViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentClinicInfoBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(ClinicsViewModel::class.java)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clinicMainModel(headerMapUniversal(requireContext()), args.clinicId)

        viewModel.errorAction.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        var isFavourite=false

        viewModel.clinicMain.observe(viewLifecycleOwner, {

            shareViewModel.clinicInfo(it)

            binding.loader.loader.visibility=View.GONE

            binding.apply {
                backToHome.setOnClickListener {
                    findNavController().popBackStack()
                }

                isFavourite=it.content.is_favorite
                if (it.content.is_favorite){
                    save.setImageResource(R.drawable.ic_saved)
                }else{
                    save.setImageResource(R.drawable.ic_savee)
                }

                closedTitle.text=it.content.closed_title
                rating.text=it.content.rating
                reviewCount.text="(${it.content.review_count})"
                type.text=it.content.type
                Picasso.get().load(it.content.poster).into(poster)

                if (it.content.closed){
                    closedTitle.setTextColor(requireContext().getColor(R.color.clinic_color))
                }else{
                    closedTitle.setTextColor(requireContext().getColor(R.color.green))
                }

                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.aboutClinic)))
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.services)))
                tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.doctors)))
                tabLayout.tabGravity = TabLayout.GRAVITY_FILL


                val adapter = PagerAdapter(childFragmentManager, tabLayout.tabCount, requireContext())
                viewPager.adapter = adapter
                tabLayout.setupWithViewPager(viewPager)
            }
        })

        binding.rate.setOnClickListener {
            DialogRate(args.clinicId, true).show(parentFragmentManager, tag)
        }

        viewModel.errorFav.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.save.setOnClickListener { someId->
            try {
                if (!isFavourite){
                    viewModel.addToFavouriteModel(headerMapUniversal(requireContext()),"clinic",  args.clinicId)
                    binding.loader.loader.visibility=View.VISIBLE
                }
            }catch (e:Exception){

            }
        }

        viewModel.addTOFavourite.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility=View.GONE
            isFavourite=true
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}