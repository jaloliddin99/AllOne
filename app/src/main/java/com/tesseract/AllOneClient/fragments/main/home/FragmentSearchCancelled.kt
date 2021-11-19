package com.tesseract.AllOneClient.fragments.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentSearchCancelledBinding

class FragmentSearchCancelled: Fragment(R.layout.fragment_search_cancelled) {

    private var _binding: FragmentSearchCancelledBinding?=null
    private val binding get() = _binding!!
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSearchCancelledBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.bookNow.backgroundTintList =
            context?.resources?.getColorStateList(R.color.red)


        val bottomSheet: View = view.findViewById(R.id.bottomSheetNestedScrollView)

        mBottomSheetBehavior=BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.bookNow.backgroundTintList =
                            context?.resources?.getColorStateList(R.color.red)
                        binding.bookNow.text=getString(R.string.cancel)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.bookNow.backgroundTintList =
                            context?.resources?.getColorStateList(R.color.green)
                        binding.bookNow.text=getString(R.string.book_now)
                    }
                }

            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

            }
        })


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}