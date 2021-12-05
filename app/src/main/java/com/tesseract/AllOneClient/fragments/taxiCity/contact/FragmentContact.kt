package com.tesseract.AllOneClient.fragments.taxiCity.contact

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.taxiCity.ContactAdapter
import com.tesseract.AllOneClient.databinding.FragmentClientContactListBinding
import com.tesseract.AllOneClient.model.taxiCity.Contact
import com.tesseract.AllOneClient.utils.hasPermission
import com.tesseract.AllOneClient.utils.requestPermissionWithRationale
import kotlinx.android.synthetic.main.fragment_client_contact_list.*

class FragmentContact : Fragment(),
    ContactAdapter.OnContactSelected {

    private var _binding: FragmentClientContactListBinding?=null
    private val binding get() = _binding!!
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactSelected: Contact

    private val contactsViewModel by viewModels<ContactViewModel>()
    private val CONTACTS_READ_REQ_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.apply {


            searchText.addTextChangedListener(textWatcher)

            backToHome.setOnClickListener {
                contactSelected.isCancelled=true
                setBackStackData("selectedContact", contactSelected, true)
            }

            select.setOnClickListener {
                if (select.alpha.toInt() == 1) {
                    contactSelected.isCancelled=false
                    setBackStackData("selectedContact", contactSelected, true)
                }
            }
        }

    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            contactAdapter.filter.filter(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }


    private fun init() {
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        contactsViewModel.contactsLiveData.observe(viewLifecycleOwner,  {
            binding.layout.loader.visibility=View.GONE
            contactAdapter = ContactAdapter(it, this@FragmentContact)
            recyclerView.adapter = contactAdapter
        })
        if (requireContext().hasPermission(Manifest.permission.READ_CONTACTS)) {
            contactsViewModel.fetchContacts()
        } else {
            requireActivity().requestPermissionWithRationale(Manifest.permission.READ_CONTACTS, CONTACTS_READ_REQ_CODE, getString(
                R.string.contact_permission_rationale))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_READ_REQ_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            contactsViewModel.fetchContacts()
        }
    }


    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    override fun onSelect(contact: Contact) {
        contactSelected = contact
        binding.select.alpha = 1f

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}