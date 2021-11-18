package com.tesseract.AllOneClient.fragments.medTurism.mainClinic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.medMain.MedTurMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainClinicViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val mainIndex=MutableLiveData<MedTurMainModel>()
    val errorM=MutableLiveData<String>()

    fun mainIndex(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getMedTurIndex(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        mainIndex.postValue(it.body())
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