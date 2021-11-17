package com.tesseract.AllOneClient.fragments.login.registration

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.MainActivity
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.createdTime
import com.tesseract.AllOneClient.constants.SaveData.loginUser
import com.tesseract.AllOneClient.constants.SaveData.saveBalance
import com.tesseract.AllOneClient.constants.SaveData.saveBirthdate
import com.tesseract.AllOneClient.constants.SaveData.saveGender
import com.tesseract.AllOneClient.constants.SaveData.saveName
import com.tesseract.AllOneClient.constants.SaveData.saveNameOnly
import com.tesseract.AllOneClient.constants.SaveData.savePhone
import com.tesseract.AllOneClient.constants.SaveData.saveUserId
import com.tesseract.AllOneClient.databinding.FragmentRegisterBinding
import com.tesseract.AllOneClient.dialogs.login.DialogDateOfBirth
import com.tesseract.AllOneClient.dialogs.login.DialogPoll
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime
import com.tesseract.AllOneClient.utils.xValue
import com.tesseract.AllOneClient.utils.yValue
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register),
    DialogPoll.OnSelectListener, DialogDateOfBirth.OnDaySelectListener{
    private var binding: FragmentRegisterBinding? = null
    private var maleFemale:String=""
    private lateinit var viewModel: RegisterViewModel

    val args:RegisterFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerBinding = FragmentRegisterBinding.bind(view)
        binding = registerBinding
        viewModel=ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding?.backToHome?.setOnClickListener {

            findNavController().popBackStack()
        }

        val location = IntArray(2)
        binding?.yourGender?.getLocationOnScreen(location)

        xValue= location[0].toFloat()
        yValue= location[1].toFloat()



        binding?.btnRegister?.setOnClickListener {
            val name = binding?.txtName?.text.toString()
            val lastname = binding?.txtLastname?.text.toString()
            val gender = binding?.yourGender?.text.toString()
            val birthday = binding?.yourBirthday?.text.toString()
            if (name.isNullOrEmpty()) {
                binding?.txtName?.error = getString(R.string.enter_your_name_please)
                return@setOnClickListener
            }
            if (lastname.isNullOrEmpty()) {
                binding?.txtLastname?.error =  getString(R.string.enter_your_lastname_please)
                return@setOnClickListener
            }
            if (gender.isNullOrEmpty()) {
                binding?.yourGender?.error =  getString(R.string.choose_your_gender)
                return@setOnClickListener
            }
            if (birthday.isNullOrEmpty()) {
                binding?.yourBirthday?.error = getString(R.string.enter_your_bday)
                return@setOnClickListener
            }

            SaveData.getData(requireContext())
            saveNameOnly(requireContext(), name)

            val finalToken = "Bearer " + SaveData.getData(requireContext())
            viewModel.register(finalToken, "$name $lastname", maleFemale, birthday)

        }

        viewModel.text.observe(requireActivity(), {
            if (it.equals("registered")){
                activity?.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }
                loginUser(requireContext(), true)
                activity?.finish()
            }
        })

        viewModel.userDetails.observe(requireActivity(), {
            it.id?.let { it1 ->
                saveUserId(
                    requireContext(),
                    it1
                )
            }
            savePhone(requireContext(), it.phone)
            saveName(requireContext(), it.name)
            saveGender(requireContext(), it.gender)
            saveBirthdate(requireContext(), it.birthdate)
            saveBalance(requireContext(), it.balance)
            createdTime(requireContext(), it.created_at)


            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }

            loginUser(requireContext(), true)

            activity?.finish()

        })


        binding?.yourGender?.setOnClickListener {
            DialogPoll(this).show(parentFragmentManager, "fragmentManager")
        }

        binding?.yourBirthday?.setOnClickListener {
            DialogDateOfBirth(getString(R.string.day_of_birth), this).show(parentFragmentManager, "DialogFragmentManager")
        }
    }

    fun getLocationOnScreen(view: View): Point {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Point(location[0], location[1])
    }

    override fun userGender(gender: String, id: Int) {
        your_gender.text = gender
        if (id==1){
            maleFemale="male"
        }else if (id==2){
            maleFemale="female"
        }
    }

    override fun selectDayListener(time: String) {
        your_birthday.text=time
    }

}