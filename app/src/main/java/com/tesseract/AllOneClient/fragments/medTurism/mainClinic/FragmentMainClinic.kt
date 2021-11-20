package com.tesseract.AllOneClient.fragments.medTurism.mainClinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.*
import com.tesseract.AllOneClient.databinding.FragmentMedTurizmEntranceBinding
import com.tesseract.AllOneClient.model.medTourism.MainMedModel
import com.tesseract.AllOneClient.model.medTourism.medMain.NearbyClinic
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularCategory
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularClinic
import com.tesseract.AllOneClient.model.medTourism.medMain.PopularDoctor
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentMainClinic : Fragment(R.layout.fragment_med_turizm_entrance), ClinicMainAdapter.OnImageClickListener, ChipAdapter.OnChipClickListener,
    PopularAdapter.PopularClinics, PopularDoctorAdapter.OnDoctorClicked, NearClinicsAdapter.OnNearByKlicked {
    private var _binding: FragmentMedTurizmEntranceBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainClinicViewModel

    private lateinit var mainMedModel: List<MainMedModel>
    private lateinit var clinicMainAdapter: ClinicMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentMedTurizmEntranceBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(MainClinicViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        loadItems()

        clinicMainAdapter=
            ClinicMainAdapter(mainMedModel, this)
        binding.recyclerView.adapter=clinicMainAdapter
        binding.recyclerView.layoutManager= GridLayoutManager(requireContext(), 2)
        binding.recyclerView.setHasFixedSize(true)

        viewModel.mainIndex(headerMapUniversal(requireContext()))

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        binding.ambulance.setOnClickListener {
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentMedAmbulance()
            findNavController().navigate(action)
        }

        binding.apply {

            viewModel.mainIndex.observe(viewLifecycleOwner, {
                recyclerViewChip.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter= ChipAdapter(this@FragmentMainClinic, it.content.popular_categories)
                }
                loader.loader.visibility=View.GONE


                recyclerViewPopular.apply {
                    layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                            lp.width = width * 6 / 8
                            return true
                        }
                    }
                    adapter= PopularAdapter(this@FragmentMainClinic, it.content.popular_clinics)
                }


                recyclerViewDoctors.apply {
                    layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                            lp.width = width * 3 / 8
                            return true
                        }
                    }
                    adapter= PopularDoctorAdapter(this@FragmentMainClinic, it.content.popular_doctors)
                }


                recyclerViewNear.apply {
                    layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter= NearClinicsAdapter(this@FragmentMainClinic, it.content.nearby_clinics)
                }

            })






        }

    }

    private fun loadItems(){
        mainMedModel= listOf(
            MainMedModel(getString(R.string.clinic), R.drawable.ic_clinics),
            MainMedModel(getString(R.string.categories), R.drawable.ic_clinic_world),
            MainMedModel(getString(R.string.doctors), R.drawable.ic_clinic_gadget),
            MainMedModel(getString(R.string.mySaved), R.drawable.ic_saved),
        )
    }

    override fun onItemClick(position: Int) {
        if (position==0){
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentClinics()
            findNavController().navigate(action)
        }
        if (position==2){
            val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentMedTurDoctors()
            findNavController().navigate(action)
        }
        if (position==3){
            val action=FragmentMainClinicDirections.actionGlobalMedOrTourFavourites("med_tourism")
            findNavController().navigate(action)
        }
    }

    override fun onChipClicked(position: PopularCategory) {

    }

    override fun onPopularClicked(position: PopularClinic) {
        Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show()
    }

    override fun onChipClicked(position: PopularDoctor) {

    }

    override fun onChipClicked(position: NearbyClinic) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}