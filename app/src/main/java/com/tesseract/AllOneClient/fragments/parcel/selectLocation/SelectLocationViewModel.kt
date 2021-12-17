package com.tesseract.AllOneClient.fragments.parcel.selectLocation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.parcel.SelectLocationData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class SelectLocationViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val data=MutableLiveData<SelectLocationData>()
    val errorMessage=MutableLiveData<String>()


    fun getLocationReverse(token: Map<String, String>, latLng: String)=viewModelScope.launch {

        try {
            repository.getLocationReverse(token, latLng).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        data.postValue(it.body()?.content!!)
                    }else{
                        errorMessage.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorMessage.postValue("Connect to internet")
                }
            }
        }catch (e: Exception){
            errorMessage.postValue("No internet")
        }


    }


}