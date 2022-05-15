package com.tesseract.AllOneClient.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.databinding.DialogContactPresenterBinding
import com.tesseract.AllOneClient.model.medTourism.doctorView.Content
import com.tesseract.AllOneClient.utils.gotoContact

class DialogContactPresenter(private val phoneNumber: List<String>) : BottomSheetDialogFragment(),
    MedPhoneAdapter.OnClickListener {

    private var _binding: DialogContactPresenterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogContactPresenterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = MedPhoneAdapter(phoneNumber, this@DialogContactPresenter)


            cancel.setOnClickListener {
                dialog?.dismiss()
            }

        }


    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}