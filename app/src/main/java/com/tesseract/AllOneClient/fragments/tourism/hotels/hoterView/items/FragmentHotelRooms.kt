package com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.items

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.Common.Common.fromWhichLayout
import com.tesseract.AllOneClient.adapter.tourism.hotel.HotelRoomAdapter
import com.tesseract.AllOneClient.databinding.FragmentHotelRoomsBinding
import com.tesseract.AllOneClient.dialogs.tourism.DialogHotelRoomView
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.FragmentHotelView
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.HotelViewModel
import com.tesseract.AllOneClient.model.tourism.hotels.hotelView.HotelRoom

class FragmentHotelRooms : Fragment(), HotelRoomAdapter.OnChipClickListener {

    private var _binding: FragmentHotelRoomsBinding? = null
    private val binding get() = _binding!!
    private val shareViewModel: HotelViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotelRoomsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            shareViewModel.mutableSearchItem.observe(viewLifecycleOwner, {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = HotelRoomAdapter(this@FragmentHotelRooms, it.content.hotel_rooms)
                }
            })
        }
    }

    override fun onItemClicked(position: HotelRoom) {
        DialogHotelRoomView(FragmentHotelView.hotelId, position.id, fromWhichLayout).show(parentFragmentManager, tag)
    }

}