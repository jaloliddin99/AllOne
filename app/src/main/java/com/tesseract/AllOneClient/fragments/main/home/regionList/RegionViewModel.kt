package com.tesseract.AllOneClient.fragments.main.home.regionList

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
                    }
                }
            }
        }catch (e:Exception){

        }
    }




}