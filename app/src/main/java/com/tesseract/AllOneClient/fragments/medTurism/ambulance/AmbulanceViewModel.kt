package com.tesseract.AllOneClient.fragments.medTurism.ambulance

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.ambulance.AmbulanceModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AmbulanceViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val ambulance=MutableLiveData<AmbulanceModel>()


    fun ambulanceObserver(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.ambulance(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        ambulance.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }
}