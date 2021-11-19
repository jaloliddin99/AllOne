package com.tesseract.AllOneClient.fragments.tourism.mainTourism

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.tourism.main.index.TourismMainIndex
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TourismMainViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val tourismMainIndex=MutableLiveData<TourismMainIndex>()

    val errorM=MutableLiveData<String>()
    fun tourismMainIndex(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.tourismMainIndex(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        tourismMainIndex.postValue(it.body())
                    }else{
                        errorM.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorM.postValue(it.body()?.message.toString())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }

}