package com.tesseract.AllOneClient.fragments.tourism.carRent.carRentMain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.CarRentIndexMain
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class IndexViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val indexMain=MutableLiveData<CarRentIndexMain>()
    val errorM=MutableLiveData<String>()

    fun indexMainRequest(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getCarRentIndexMain(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        indexMain.postValue(it.body())
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