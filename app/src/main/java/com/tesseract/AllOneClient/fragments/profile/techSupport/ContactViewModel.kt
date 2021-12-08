package com.tesseract.AllOneClient.fragments.profile.techSupport

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.ContactsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val contacts=MutableLiveData<ContactsModel>()
    val contactError=MutableLiveData<String>()

    fun contacts(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getContacts(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        contacts.postValue(it.body())
                    }else{
                        contactError.postValue(it.body()?.message.toString())
                    }
                }else{
                    contactError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            contactError.postValue(e.message)
        }
    }


}