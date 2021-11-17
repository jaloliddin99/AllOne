package com.tesseract.AllOneClient.dialogs.medTur

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.DialogRateClinicDriverBinding
import com.tesseract.AllOneClient.fragments.medTurism.clinicInfo.ClinicsViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogRate(private val clinicId:Int) : BottomSheetDialogFragment() {
    private var binding: DialogRateClinicDriverBinding?=null

    private lateinit var viewModel:ClinicsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogRateClinicDriverBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(ClinicsViewModel::class.java)

        return binding!!.root
    }

    var ratingBarItem:Int=-1
    var commentText:String=""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding?.apply {
            loader.loader.visibility=View.GONE
            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingBarItem=rating.toInt()
            }

            send.setOnClickListener {
                commentText=comment.text.toString()

                if (commentText.isNotEmpty()&&ratingBarItem!=-1){
                    viewModel.ratingObserver(headerMapUniversal(requireContext()), clinicId, ratingBarItem, commentText)
                    loader.loader.visibility=View.VISIBLE

                }
            }
            viewModel.ratingListener.observe(viewLifecycleOwner, {
                binding?.loader?.loader?.visibility=View.GONE
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            })

        }

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

}