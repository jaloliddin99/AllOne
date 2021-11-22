package com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentHotelContactsItemBinding
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.HotelViewModel
import dagger.hilt.android.AndroidEntryPoint


class FragmentHotelContacts:Fragment(), MedPhoneAdapter.OnClickListener {
    private var _binding:FragmentHotelContactsItemBinding?=null
    private val binding get() = _binding!!

    private val shareViewModel: HotelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentHotelContactsItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {

                recyclerView.apply {
                    layoutManager=
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter= ClinicImagesAdapter(it.content.gallery)
                }
                addr.text=it.content.addr
                recyclerViewPhones.layoutManager=
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter= MedPhoneAdapter(it.content.phone_number, this@FragmentHotelContacts)

                telegram.text=it.content.telegram
                website.text=it.content.website
                description.text=it.content.description



            })
        }




    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding=null
    }

    override fun onChipClicked(position: String) {

    }

}