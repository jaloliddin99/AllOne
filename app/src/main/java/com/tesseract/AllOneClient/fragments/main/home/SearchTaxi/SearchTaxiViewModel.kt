package com.tesseract.AllOneClient.fragments.main.home.SearchTaxi

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchTaxiViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val searchTaxiResponse=MutableLiveData<Content>()
    val interAreaError=MutableLiveData<String>()
    fun searchRegionTaxiOrder(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.searchOrderRegion(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        searchTaxiResponse.postValue(it.body()?.content)
                    }else{
                        interAreaError.postValue(it.body()?.message.toString())
                    }
                }else{
                    interAreaError.postValue(it.message())
                }

            }
        }catch (e:Exception){
            interAreaError.postValue(e.message)
        }
    }
    val parcelSearchModel=MutableLiveData<com.tesseract.AllOneClient.model.parcel.parcelSearch.Content>()
    val parcelError=MutableLiveData<String>()

    fun parcelSearchRequest(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.parcelSearch(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        parcelSearchModel.postValue(it.body()?.content)
                    }else{
                        parcelError.postValue(it.body()?.message.toString())
                    }
                }else{
                    parcelError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            parcelError.postValue(e.message)
        }
    }


}