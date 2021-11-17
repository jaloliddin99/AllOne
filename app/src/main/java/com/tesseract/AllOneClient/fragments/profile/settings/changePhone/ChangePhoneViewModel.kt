package com.tesseract.AllOneClient.fragments.profile.settings.changePhone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.login.UsedDetailsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {

    val text=MutableLiveData<String>()


    fun sendCode(token: Map<String, String>, phone:String) = viewModelScope.launch {
        repository.sendCode(token, phone).let {
            if (it.isSuccessful){
                if (it.body()?.success == true){
                    text.postValue("Ok")
                }else{
                    text.postValue("No")
                }
            }else{
                text.postValue(it.message())
            }
        }
    }

    fun isNotEmpty(text:String):Boolean{
        if (text.isNullOrEmpty()){
            return false
        }
        return true
    }

    val userDetails=MutableLiveData<UsedDetailsModel>()

    fun updatePhone(token: Map<String, String>, phone:String, code: String)=viewModelScope.launch {
        repository.updatePhone(token, phone, code).let {
            if (it.isSuccessful){
                if (it.body()?.success==true){
                    userDetails.postValue(it.body()?.content)
                }
            }
        }
    }

}