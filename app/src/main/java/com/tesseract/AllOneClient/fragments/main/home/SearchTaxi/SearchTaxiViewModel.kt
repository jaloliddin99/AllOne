package com.tesseract.AllOneClient.fragments.main.home.SearchTaxi

import android.util.Log
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
    fun searchRegionTaxiOrder(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.searchOrderRegion(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        searchTaxiResponse.postValue(it.body()?.content)
                    }
                }
                Log.i("success message", ""+it.code()+it.message())
            }
        }catch (e:Exception){

        }
    }
    val parcelSearchModel=MutableLiveData<com.tesseract.AllOneClient.model.parcel.parcelSearch.Content>()

    fun parcelSearchRequest(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.parcelSearch(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        parcelSearchModel.postValue(it.body()?.content)
                    }
                }
            }
        }catch (e:Exception){

        }
    }


}