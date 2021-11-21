package com.tesseract.AllOneClient.fragments.main.home.regionList

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegionViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {


    var regionDetails=MutableLiveData<List<GetRegionDetails>>()
    fun getRegionList(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getRegions(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        regionDetails.postValue(it.body()?.getRegionList)
                    }else{
                        Log.i(TAG, "getRegionList: aadwawd ${it.message()} ${it.code()}")
                    }
                }else{
                    Log.i(TAG, "getRegionList: adwwada ${it.message()} ${it.code()}")
                }
            }
        }catch (e:Exception){
            Log.i(TAG, "getRegionList: ${e.message}")
        }
    }




}