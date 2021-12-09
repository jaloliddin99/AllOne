package com.tesseract.AllOneClient.fragments.taxiCity.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.LocationSearch.LocationSearchList
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.SavedLocationData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySelectLocationViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {


    var savedLocations=MutableLiveData<List<SavedLocationData>>()

    fun savedLocations(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getSavedAddresses(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        savedLocations.postValue(it.body()?.getDistrictContent!!)
                    }
                }
                Log.i(TAG, "savedLocations Exception333: ${it.message()} ${it.code()}")
            }
        }catch (e:Exception){
            Log.i(TAG, "savedLocations Exception: ${e.message}")
        }
    }

    val locationSearchError= MutableLiveData<String>()
    val locationList= MutableLiveData<List<LocationSearchList>>()

    fun getLocationSearch(token: Map<String, String>, location:String)=viewModelScope.launch {
        try {
            repository.getLocationSearch(token, location).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        locationList.postValue(it.body()?.getDistrictContent!!)
                    }else{
                        locationSearchError.postValue(it.body()?.message!!)
                    }
                }
            }
        }catch (e:Exception){

        }
    }
}