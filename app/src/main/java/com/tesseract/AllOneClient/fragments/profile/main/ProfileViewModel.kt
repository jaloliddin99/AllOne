package com.tesseract.AllOneClient.fragments.profile.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: NetworkRepository): ViewModel() {

    val updateAvatar=MutableLiveData<ClinicAddToFavouriteModel>()
    var errorM=MutableLiveData<String>()

    fun updateAvatar(token: Map<String, String>, avatar:String)=viewModelScope.launch {
        try {
            repository.updateAvatar(token, avatar).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        updateAvatar.postValue(it.body())
                    }else{
                        errorM.postValue(it.body()?.message)
                    }
                }else{
                    errorM.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }

}