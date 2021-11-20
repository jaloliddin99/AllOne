package com.tesseract.AllOneClient.fragments.tourism.packagesView

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
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.GalleryAdapter
import com.tesseract.AllOneClient.adapter.tourism.tourPackages.TourismPagerAdapter
import com.tesseract.AllOneClient.databinding.FragmentTourPackagesViewBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentPackagesView : Fragment() {
    private var _binding: FragmentTourPackagesViewBinding? = null
    private lateinit var viewModel: PackageViewModel
    private val binding get() = _binding!!
    private val args: FragmentPackagesViewArgs by navArgs()
    private val shareViewModel: PackageViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTourPackagesViewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(PackageViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.packageView(headerMapUniversal(requireContext()), args.packageId)

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        var isFavourite = false

        viewModel.packageView.observe(viewLifecycleOwner, {
            shareViewModel.clinicInfo(it)

            binding.loader.loader.visibility = View.GONE
            binding.apply {
                backToHome.setOnClickListener {
                    findNavController().popBackStack()
                }

                isFavourite = it.content.is_favorite
                if (it.content.is_favorite) {
                    save.setImageResource(R.drawable.ic_saved)
                } else {
                    save.setImageResource(R.drawable.ic_savee)
                }
                name.text=it.content.name
                route.text=it.content.route
                days.text=it.content.days
                ageFrom.text=it.content.age_from
                season.text=it.content.season
                visa.text=it.content.visa
                priceFrom.text=it.content.price_from
                setCard(it.content.gallery)

                tabLayout.addTab(tabLayout.newTab())
                tabLayout.addTab(tabLayout.newTab())
                tabLayout.tabGravity = TabLayout.GRAVITY_FILL


                val adapter = TourismPagerAdapter(childFragmentManager, tabLayout.tabCount, requireContext())
                viewPager2.adapter = adapter
                tabLayout.setupWithViewPager(viewPager2)
                nestedScrollView.isFillViewport=true

            }

        })

        viewModel.errorFav.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility = View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.save.setOnClickListener { someId ->
            try {
                if (!isFavourite) {
                    viewModel.addToFavourite(headerMapUniversal(requireContext()), args.packageId)
                    binding.loader.loader.visibility = View.VISIBLE
                }
            } catch (e: Exception) {

            }
        }

        viewModel.addToFav.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility = View.GONE
            isFavourite = true
        })
    }

    private fun setCard(banner: List<String>) {
        binding.viewPager.clipToPadding = false
        binding.viewPager.adapter =
            GalleryAdapter(this, banner)
        binding.viewPager.pageMargin = 48

        binding.indicator.setViewPager(binding.viewPager)

        binding.viewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}