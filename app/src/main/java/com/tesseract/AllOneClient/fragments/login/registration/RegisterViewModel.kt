package com.tesseract.AllOneClient.fragments.login.registration

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.login.UsedDetailsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: NetworkRepository) :ViewModel() {

    val text=MutableLiveData<String>()
    val userDetails=MutableLiveData<UsedDetailsModel>()

    val errorMessage=MutableLiveData<String>()

    fun register(token: String, fullName:String, gender: String, birthday: String)=viewModelScope.launch {
        try {
            repository.register(token, fullName, gender, birthday).let {

                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        userDetails.postValue(it.body()?.content)
                        text.postValue("registered")
                    }else{
                        errorMessage.postValue(it.message())
                    }
                }else{
                    errorMessage.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorMessage.postValue(e.message)
        }

    }

}