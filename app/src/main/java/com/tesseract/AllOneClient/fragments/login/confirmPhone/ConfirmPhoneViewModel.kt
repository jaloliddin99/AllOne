package com.tesseract.AllOneClient.fragments.login.confirmPhone

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.constants.SaveData.createdTime
import com.tesseract.AllOneClient.constants.SaveData.saveBalance
import com.tesseract.AllOneClient.constants.SaveData.saveBirthdate
import com.tesseract.AllOneClient.constants.SaveData.saveGender
import com.tesseract.AllOneClient.constants.SaveData.saveName
import com.tesseract.AllOneClient.constants.SaveData.savePhone
import com.tesseract.AllOneClient.constants.SaveData.saveUserId
import com.tesseract.AllOneClient.model.login.UsedDetailsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmPhoneViewModel @Inject constructor(private val repository: NetworkRepository): ViewModel (){

    val textPhone=MutableLiveData<String>()
    val userToken=MutableLiveData<String>()
    val userDetails=MutableLiveData<UsedDetailsModel>()

    fun loginUser(phone: String, sendCode: String)=viewModelScope.launch {
        repository.loginUser(phone, sendCode).let {
            if (it.isSuccessful){
                if (it.body()?.success==true){
                    val token:String= it.body()?.content?.token.toString()

                    userToken.postValue(token)

                    if (it.body()?.content?.data.toString()=="null"){

                        textPhone.postValue("not_registered")
                    }else{
                        textPhone.postValue("registered")
                        userDetails.postValue(it.body()?.content?.data)
                    }

                }
            }
        }
    }
}