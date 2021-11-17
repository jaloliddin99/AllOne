package com.tesseract.AllOneClient.fragments.profile.settings.changeData

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentChangeDataBinding
import com.tesseract.AllOneClient.dialogs.login.DialogDateOfBirth
import com.tesseract.AllOneClient.dialogs.login.DialogPoll
import com.tesseract.AllOneClient.fragments.login.registration.RegisterViewModel
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

@AndroidEntryPoint
class ChangeDataFragment: Fragment(R.layout.fragment_change_data),
    DialogPoll.OnSelectListener, DialogDateOfBirth.OnDaySelectListener{
    private var maleFemale:String=""
    lateinit var dialog: Dialog
    private var changeDataFragment: FragmentChangeDataBinding?=null
    private lateinit var viewModel: ChangeDataViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding=FragmentChangeDataBinding.bind(view)
        changeDataFragment=binding
        viewModel= ViewModelProvider(this).get(ChangeDataViewModel::class.java)


        changeDataFragment?.name?.setText(SaveData.getName(requireContext()))
        changeDataFragment?.selectDate?.setText(SaveData.getBirthdate(requireContext()))
        changeDataFragment?.selectGender?.setText(SaveData.getGender(requireContext()))

        dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loader)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))



        changeDataFragment?.backToHome?.setOnClickListener {
            findNavController().popBackStack()
        }

        changeDataFragment?.update?.setOnClickListener {
            val name = changeDataFragment?.name?.text.toString()
            val gender = changeDataFragment?.selectGender?.text.toString()
            val birthday = changeDataFragment?.selectDate?.text.toString()
            if (name.isNullOrEmpty()) {
                changeDataFragment?.name?.error = getString(R.string.enter_your_name_please)
                return@setOnClickListener
            }

            if (gender.isNullOrEmpty()) {
                changeDataFragment?.selectGender?.error =  getString(R.string.choose_your_gender)
                return@setOnClickListener
            }
            if (birthday.isNullOrEmpty()) {
                changeDataFragment?.selectDate?.error = getString(R.string.enter_your_bday)
                return@setOnClickListener
            }

            dialog.show()
            viewModel.update(headerMapUniversal(requireContext()), name, maleFemale, birthday)
        }

        viewModel.userDetails.observe(requireActivity(), {
            it.id?.let { it1 ->
                SaveData.saveUserId(
                    requireContext(),
                    it1
                )
            }
            SaveData.savePhone(requireContext(), it.phone)
            SaveData.saveName(requireContext(), it.name)
            SaveData.saveGender(requireContext(), it.gender)
            SaveData.saveBirthdate(requireContext(), it.birthdate)
            SaveData.saveBalance(requireContext(), it.balance)
            SaveData.createdTime(requireContext(), it.created_at)

            dialog.dismiss()

        })

        changeDataFragment?.selectGender?.setOnClickListener {
            DialogPoll(this).show(parentFragmentManager, "fragmentManager")
        }

        changeDataFragment?.selectDate?.setOnClickListener {
            DialogDateOfBirth(getString(R.string.day_of_birth), this).show(parentFragmentManager, "fragmentManager")
        }
    }

    override fun userGender(gender: String, id: Int) {
        changeDataFragment?.selectGender?.text = gender
        if (id==1){
            maleFemale="male"
        }else if (id==2){
            maleFemale="female"
        }
    }

    override fun selectDayListener(time: String) {
        changeDataFragment?.selectDate?.text = time
    }

}