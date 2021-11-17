package com.tesseract.AllOneClient.fragments.taxiCity.contact

import android.Manifest
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.taxiCity.ContactAdapter
import com.tesseract.AllOneClient.databinding.FragmentClientContactListBinding
import com.tesseract.AllOneClient.model.taxiCity.ContactModel
import kotlinx.android.synthetic.main.fragment_client_contact_list.*

class FragmentContact : Fragment(R.layout.fragment_client_contact_list), ContactAdapter.OnContactSelected {

    private lateinit var binding: FragmentClientContactListBinding
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contactSelected:ContactModel

    private var hasPermission: Boolean=false

    private var contactModel = ArrayList<ContactModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentClientContactListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getContactList()

        binding.apply {
            recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            contactAdapter= ContactAdapter(contactModel, this@FragmentContact)
            recyclerView.adapter=contactAdapter
            recyclerView.setHasFixedSize(true)

            .apply {
                searchText.addTextChangedListener(textWatcher)
            }
            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            select.setOnClickListener {
                if (select.alpha.toInt()==1){
                    setBackStackData("selectedContact", contactSelected, true)
                }
            }
        }


    }

    private val textWatcher=object :TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            contactAdapter.filter.filter(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }


    @SuppressLint("Recycle")
    private fun getContactList() {
        val cursor: Cursor? = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var phone =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                phone = phone.replace("-", "")
                phone = phone.replace("(", "")
                phone = phone.replace(")", "")

                val contact = ContactModel( name, phone)
                contactModel.add(contact)
            }
        }
    }

    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }

    override fun onSelect(contact: ContactModel) {
        contactSelected=contact
        binding.select.alpha=1f

    }

}