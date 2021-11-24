package com.tesseract.AllOneClient.fragments.medTurism.doctorView

import android.annotation.SuppressLint
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
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.doctors.DoctorsClinicAdapter
import com.tesseract.AllOneClient.databinding.FragmentMedTurDoctorViewBinding
import com.tesseract.AllOneClient.dialogs.medTur.DialogRate
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.model.medTourism.doctorView.Clinic
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class FragmentDoctorView:Fragment(), DoctorsClinicAdapter.OnClickListener, MedPhoneAdapter.OnClickListener {
    private var _binding:FragmentMedTurDoctorViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DoctorViewModel
    private lateinit var viewModel2:ClinicsViewModel
    private val args:FragmentDoctorViewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentMedTurDoctorViewBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(DoctorViewModel::class.java)
        viewModel2=ViewModelProvider(this).get(ClinicsViewModel::class.java)

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isFavourite=false
        viewModel.doctorViewMainModel(headerMapUniversal(requireContext()), args.doctorId)

        viewModel.errorM.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })


        binding.apply {
            viewModel.doctorViewMainModel.observe(viewLifecycleOwner, {
                backToHome.setOnClickListener {
                    findNavController().popBackStack()
                }

                isFavourite=it.content.is_favorite
                if (it.content.is_favorite){
                    save.setImageResource(R.drawable.ic_saved)
                }else{
                    save.setImageResource(R.drawable.ic_savee)
                }
                rating.text=it.content.rating
                reviewCount.text="(${it.content.review_count})"
                type.text=it.content.type
                Picasso.get().load(it.content.poster).into(poster)


                addr.text=it.content.addr
                recyclerViewPhones.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter=MedPhoneAdapter(it.content.phone_number, this@FragmentDoctorView)


                telegram.text=it.content.telegram
                workTime.text=it.content.work_time
                description.text=it.content.description


                recyclerView.apply {
                    layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter=DoctorsClinicAdapter(it.content.clinics, this@FragmentDoctorView)
                }

                loader.loader.visibility=View.GONE
            })
        }
        viewModel2.errorFav.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        binding.rate.setOnClickListener {
            DialogRate(args.doctorId, false).show(parentFragmentManager, tag)
        }
        binding.save.setOnClickListener { someId->
            try {
                if (!isFavourite){
                    viewModel2.addToFavouriteModel(headerMapUniversal(requireContext()), "doctor",  args.doctorId)
                    binding.loader.loader.visibility=View.VISIBLE
                }
            }catch (e: Exception){

            }
        }

        viewModel2.addTOFavourite.observe(viewLifecycleOwner, {
            binding.save.setImageResource(R.drawable.ic_saved)
            binding.loader.loader.visibility=View.GONE
            isFavourite=true
        })

    }

    override fun onChipClicked(position: Clinic) {

    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}