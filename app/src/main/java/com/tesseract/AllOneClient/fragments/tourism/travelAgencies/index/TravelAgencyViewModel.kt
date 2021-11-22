package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.index

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.tourism.agency.index.AgencyIndex
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TravelAgencyViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val agencyIndex=MutableLiveData<AgencyIndex>()
    val errorM=MutableLiveData<String>()

    fun startAgencyIndex(token:Map<String, String>, query:String, countryId:Int, sort:String){
        agencyIndex(token, query, countryId, sort)
    }

    fun agencyIndex(token:Map<String, String>, query:String, countryId:Int, sort:String)=viewModelScope.launch {
        try {
            repository.getTravelAgencies(token, query, countryId, sort, Common.travelPagerId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        agencyIndex.postValue(it.body())
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