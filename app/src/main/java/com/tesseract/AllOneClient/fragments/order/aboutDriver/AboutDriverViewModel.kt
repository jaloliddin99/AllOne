package com.tesseract.AllOneClient.fragments.order.aboutDriver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.aboutDriverModel.AboutDriverData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AboutDriverViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val aboutDriverData=MutableLiveData<AboutDriverData>()
    val errorMessage=MutableLiveData<String>()


    fun getAboutDriver(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.getAboutDriver(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        aboutDriverData.postValue(it.body()?.content)
                    }else{
                        errorMessage.postValue("No internet")
                    }
                }
            }

        }catch (e: Exception){
            errorMessage.postValue("No internet")
        }
    }



}