package com.tesseract.AllOneClient.fragments.medTurism.mainClinic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.*
import com.tesseract.AllOneClient.databinding.FragmentMedTurizmEntranceBinding
import com.tesseract.AllOneClient.model.medTourism.MainMedModel

class FragmentMainClinic : Fragment(R.layout.fragment_med_turizm_entrance)
    , ClinicMainAdapter.OnImageClickListener, ChipAdapter.OnChipClickListener,
    PopularAdapter.OnChipClickListener,
    PopularDoctorAdapter.OnChipClickListener, NearClinicsAdapter.OnChipClickListener {
    private var binding: FragmentMedTurizmEntranceBinding?=null

    private lateinit var mainMedModel: List<MainMedModel>
    private lateinit var clinicMainAdapter: ClinicMainAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMedTurizmEntranceBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        loadItems()

        clinicMainAdapter=
            ClinicMainAdapter(mainMedModel, this)
        binding?.recyclerView?.adapter=clinicMainAdapter
        binding?.recyclerView?.layoutManager= GridLayoutManager(requireContext(), 2)
        binding?.recyclerView?.setHasFixedSize(true)


        binding?.apply {
            recyclerViewChip.apply {
                layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter= ChipAdapter(this@FragmentMainClinic)
            }

            recyclerViewPopular.apply {
                layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                        lp.width = width * 6 / 8
                        return true
                    }
                }
                adapter= PopularAdapter(this@FragmentMainClinic)
            }

            recyclerViewDoctors.apply {
                layoutManager= object : LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                        lp.width = width * 3 / 8
                        return true
                    }
                }
                adapter= PopularDoctorAdapter(this@FragmentMainClinic)
            }

            recyclerViewNear.apply {
                layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter= NearClinicsAdapter(this@FragmentMainClinic)
            }
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
        val action=FragmentMainClinicDirections.actionFragmentMainClinicToFragmentClinics()
        findNavController().navigate(action)
    }

    override fun onChipClicked(position: Int) {

    }
}