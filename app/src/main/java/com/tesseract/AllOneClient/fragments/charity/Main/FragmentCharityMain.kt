package com.tesseract.AllOneClient.fragments.charity.Main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData.formatPhone
import com.tesseract.AllOneClient.databinding.FragmentGoodBinding
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCharityMain:Fragment() {
    private var _binding:FragmentGoodBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: CharityMainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentGoodBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CharityMainViewModel::class.java)
        return binding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.charityMainGet(headerMapUniversal(requireContext()))

        binding.loader.loader.visibility=View.VISIBLE
        charity()

        val drawerLayout: DrawerLayout =requireActivity().findViewById(R.id.drawerLayout)

        binding.drawerIcon.setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(Gravity.START)
            else drawerLayout.closeDrawer(Gravity.END);
            drawerLayout.openDrawer(Gravity.START)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun charity(){
        binding.apply {
            history.setOnClickListener {
                val action=FragmentCharityMainDirections.actionFragmentGoodMainToFragmentDonationHistory()
                findNavController().navigate(action)
            }

            makeDonation.setOnClickListener {
                val action=FragmentCharityMainDirections.actionFragmentGoodMainToFragmentMakeDonation()
                findNavController().navigate(action)
            }

            viewModel.charityErrorMain.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility=View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })

            viewModel.charityMain.observe(viewLifecycleOwner, {
                binding.loader.loader.visibility=View.GONE
                level.text=it.level.toString()
                trips.text="${it.level}/ ${it.trips}"
                donatedOverall.text=  formatPhone(it.donated_overall)+" "+getString(R.string.summa1)
                donatedFromTripsAll.text= formatPhone(it.donated_from_trips_all)+" "+getString(R.string.summa1)
                donatedFromTripsToday.text= formatPhone(it.donated_from_trips_today)+" "+getString(R.string.summa1)


            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}