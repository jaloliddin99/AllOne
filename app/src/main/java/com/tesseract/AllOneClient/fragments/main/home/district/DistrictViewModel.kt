package com.tesseract.AllOneClient.fragments.main.home.district

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.getDistricts.DistrictList
import com.tesseract.AllOneClient.model.home.getDistricts.RegionName
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DistrictViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val regionName=MutableLiveData<RegionName>()
    val districtList=MutableLiveData<List<DistrictList>>()

    fun getDistrictDetails(token: Map<String, String>, regionId: String)=viewModelScope.launch {
        repository.getDistricts(token, regionId).let {
            if (it.isSuccessful){
                if (it.body()?.success==true){
                    regionName.postValue(it.body()?.getDistrictContent?.region)
                    districtList.postValue(it.body()?.getDistrictContent?.districtList)
                }
            }
        }
    }
}