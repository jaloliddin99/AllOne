package com.tesseract.AllOneClient.fragments.profile.changeLang

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
class ChangeLangViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {


    val changeLang=MutableLiveData<ClinicAddToFavouriteModel>()
    val errorM=MutableLiveData<String>()

    fun changeLanguage(token:Map<String, String>, lang:String)=viewModelScope.launch {
        try {
            repository.changeLang(token, lang).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        changeLang.postValue(it.body())
                    }else{
                        errorM.postValue(it.message())
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