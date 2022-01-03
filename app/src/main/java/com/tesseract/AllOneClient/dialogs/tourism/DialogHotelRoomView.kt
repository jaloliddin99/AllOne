package com.tesseract.AllOneClient.dialogs.tourism

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.tourism.hotel.RoomGallery
import com.tesseract.AllOneClient.databinding.DialogHotelRoomViewBinding
import com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView.HotelViewModel
import com.tesseract.AllOneClient.model.tourism.hotels.roomView.HotelFacility
import com.tesseract.AllOneClient.utils.dipToPixels
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogHotelRoomView(private val hotelId:Int, private val roomId:Int, private val fromWhichLayout:Int):BottomSheetDialogFragment() {

    private var _binding:DialogHotelRoomViewBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: HotelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    private var tourismOrMed=""
    private var hotelOrSan=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= DialogHotelRoomViewBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(HotelViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fromWhichLayout==0){
            tourismOrMed="tourism"
            hotelOrSan="hotel"
        }else{
            tourismOrMed="med_tourism"
            hotelOrSan="sanatorium"
        }

        viewModel.roomError.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
        })

        binding.cancel.setOnClickListener {
            dialog?.dismiss()
        }

        viewModel.roomView(headerMapUniversal(requireContext()),tourismOrMed, hotelOrSan, hotelId, roomId)

        viewModel.roomView.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            binding.aboutRoom.text=it.content.about_room
            binding.description.text=it.content.description
            binding.name.text=it.content.name
            binding.price.text=it.content.price

            setCard(it.content.gallery)
            implementFlowLayout(it.content.hotel_facilities)

        })

    }

    private fun setCard(banner: List<String>) {
        binding.viewPager.clipToPadding = false
        binding.viewPager.adapter =
            RoomGallery(this, banner)
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
            button.setPadding(
                dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt(),
                dipToPixels(requireContext(), 8f).toInt()
            )

            binding.flowLayout.addView(button)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }


}