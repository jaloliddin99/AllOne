package com.tesseract.AllOneClient.dialogs.medTur

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.medTourism.MedPhoneAdapter
import com.tesseract.AllOneClient.databinding.DialogClinicDoctorBinding
import com.tesseract.AllOneClient.dialogs.DialogContactPresenter
import com.tesseract.AllOneClient.fragments.medTurism.doctorView.DoctorViewModel
import com.tesseract.AllOneClient.utils.gotoContact
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDoctorView(private val doctorId: Int) : BottomSheetDialogFragment(), MedPhoneAdapter.OnClickListener {
    private var binding: DialogClinicDoctorBinding? = null
    private lateinit var viewModel: DoctorViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogClinicDoctorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(DoctorViewModel::class.java)
        return binding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.errorM.observe(viewLifecycleOwner, {
            binding?.loader?.loader?.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.doctorViewMainModel(headerMapUniversal(requireContext()), doctorId)
        binding?.apply {

            viewModel.doctorViewMainModel.observe(viewLifecycleOwner, {
                rating.text = it.content.rating
                reviewCount.text = "(${it.content.review_count})"

                type.text = it.content.type
                Picasso.get().load(it.content.poster).into(poster)
                if (it.content.telegram.isEmpty()){
                    telegramLinear.visibility=View.GONE
                }else{
                    telegramLinear.visibility=View.VISIBLE
                    telegram.text = it.content.telegram
                }
                workTime.text = it.content.work_time
                description.text = it.content.description

                loader.loader.visibility = View.GONE

                recyclerViewPhones.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                recyclerViewPhones.adapter=MedPhoneAdapter(it.content.phone_number, this@DialogDoctorView)

                phoneNumberClicked.setOnClickListener {hello->
                    if (it.content.phone_number.isEmpty()){
                        return@setOnClickListener
                    }
                    DialogContactPresenter(it.content.phone_number).show(parentFragmentManager, tag)
                }
            })


            cancel.setOnClickListener {
                dialog?.dismiss()

            }
        }

    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }

    override fun onChipClicked(position: String) {
        gotoContact(position, requireContext())
    }

}