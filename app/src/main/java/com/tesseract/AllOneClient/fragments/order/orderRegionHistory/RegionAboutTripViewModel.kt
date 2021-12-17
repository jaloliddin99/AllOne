package com.tesseract.AllOneClient.fragments.order.orderRegionHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.getTaxiOrderHistory.OrderRegionATData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegionAboutTripViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val historyView=MutableLiveData<OrderRegionATData>()
    val errorCatch=MutableLiveData<String>()

    fun getTaxiOrderHistory(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.getTaxiOrderHistory(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        historyView.postValue(it.body()?.content!!)
                    }else{
                        errorCatch.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorCatch.postValue("no internet")
                }
            }
        }catch (e:Exception){
            errorCatch.postValue("no internet")
        }
    }



}