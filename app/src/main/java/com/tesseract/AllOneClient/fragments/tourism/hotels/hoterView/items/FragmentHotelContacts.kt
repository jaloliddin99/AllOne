package com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.adapter.medTourism.clinics.ClinicImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentHotelContactsItemBinding
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.HotelViewModel
import com.tesseract.AllOneClient.model.tourism.hotels.hotelView.HotelFacility
import com.tesseract.AllOneClient.utils.dipToPixels
import com.tesseract.AllOneClient.utils.gotoContact


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

                implementFlowLayout(it.content.hotel_facilities)

            })
        }




    }

    private fun implementFlowLayout(hotelFacility: List<HotelFacility>){

        val buttonLayoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        buttonLayoutParams.setMargins(
            0,
            dipToPixels(requireContext(), 8f).toInt(),
            dipToPixels(requireContext(), 8f).toInt(),
            0)
        binding.flowLayout.removeAllViews()

        for (i in hotelFacility.indices) {
            val button = TextView(requireActivity())
            val button2= ImageView(requireContext())
            Picasso.get().load(hotelFacility[i].icon).into(button2)

            button.text=hotelFacility[i].name
            button.textSize = 13f

            button.setTextColor(requireContext().getColor(R.color.black))

            button.setBackgroundResource(R.drawable.flow_layout_item_unselected)

            button2.layoutParams=buttonLayoutParams
            button.layoutParams = buttonLayoutParams
            button.setPadding(dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt()
            )

            binding.flowLayout.addView(button)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }

}