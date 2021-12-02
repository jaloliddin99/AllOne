package com.tesseract.AllOneClient.fragments.profile.settings.changeData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentChangeDataBinding
import com.tesseract.AllOneClient.dialogs.login.DialogDateOfBirth
import com.tesseract.AllOneClient.dialogs.login.DialogPoll
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

@AndroidEntryPoint
class ChangeDataFragment: Fragment(R.layout.fragment_change_data),
    DialogPoll.OnSelectListener, DialogDateOfBirth.OnDaySelectListener{
    private var maleFemale:String=""
    private var _changeDataFragment: FragmentChangeDataBinding?=null
    private val changeDataFragment get() = _changeDataFragment!!
    private lateinit var viewModel: ChangeDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _changeDataFragment= FragmentChangeDataBinding.inflate(inflater, container, false)

        return changeDataFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(this).get(ChangeDataViewModel::class.java)

        changeDataFragment.loader.loader.visibility=View.GONE


        changeDataFragment.name.setText(SaveData.getName(requireContext()))
        changeDataFragment.selectDate.setText(SaveData.getBirthdate(requireContext()))
        changeDataFragment.selectGender.setText(SaveData.getGender(requireContext()))


        changeDataFragment.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        changeDataFragment.update.setOnClickListener {
            val name = changeDataFragment.name.text.toString()
            val gender = changeDataFragment.selectGender.text.toString()
            val birthday = changeDataFragment.selectDate.text.toString()
            if (name.isEmpty()) {
                changeDataFragment.name.error = getString(R.string.enter_your_name_please)
                return@setOnClickListener
            }

            if (gender.isEmpty()) {
                changeDataFragment.selectGender.error =  getString(R.string.choose_your_gender)
                return@setOnClickListener
            }
            if (birthday.isEmpty()) {
                changeDataFragment.selectDate.error = getString(R.string.enter_your_bday)
                return@setOnClickListener
            }

            changeDataFragment.loader.loader.visibility=View.VISIBLE
            viewModel.update(headerMapUniversal(requireContext()), name, maleFemale, birthday)
        }

        viewModel.userDetails.observe(requireActivity(), {
            changeDataFragment.loader.loader.visibility=View.GONE
            it.id.let { it1 ->
                if (it1 != null) {
                    SaveData.saveUserId(
                        requireContext(),
                        it1
                    )
                }
            }
            SaveData.savePhone(requireContext(), it.phone)
            SaveData.saveName(requireContext(), it.name)
            SaveData.saveGender(requireContext(), it.gender)
            SaveData.saveBirthdate(requireContext(), it.birthdate)
            SaveData.saveBalance(requireContext(), it.balance)
            SaveData.createdTime(requireContext(), it.created_at)



        })

        changeDataFragment.selectGender.setOnClickListener {
            DialogPoll(this).show(parentFragmentManager, "fragmentManager")
        }

        changeDataFragment.selectDate.setOnClickListener {
            DialogDateOfBirth(getString(R.string.day_of_birth), this).show(parentFragmentManager, "fragmentManager")
        }
    }

    override fun userGender(gender: String, id: Int) {
        changeDataFragment.selectGender.text = gender
        if (id==1){
            maleFemale="male"
        }else if (id==2){
            maleFemale="female"
        }
    }

    override fun selectDayListener(time: String) {
        changeDataFragment.selectDate.text = time
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _changeDataFragment=null
    }
}