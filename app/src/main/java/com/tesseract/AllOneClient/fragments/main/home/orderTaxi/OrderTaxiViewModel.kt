package com.tesseract.AllOneClient.fragments.main.home.orderTaxi

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffParcelListModel
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffPlaceListModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class OrderTaxiViewModel @Inject constructor(private val repository: NetworkRepository) :
    ViewModel() {

    val parcelList = MutableLiveData<List<RouteTariffParcelListModel>>()
    val placeList = MutableLiveData<List<RouteTariffPlaceListModel>>()

    fun getRouteTariffPrices(
        token: Map<String, String>,
        orderType: String,
        startPoint: String,
        endPoint: String
    ) = viewModelScope.launch {
        try {
            repository.getRouteTariffPrices(token, orderType, startPoint, endPoint).let {
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        parcelList.postValue(it.body()?.content?.parcels!!)
                        placeList.postValue(it.body()?.content?.places!!)
                    }else{
                    }
                }else{
                    Log.i(TAG, "getRouteTariffPres: awdwdawd${it.message()}")
                }
            }
        }catch (e:Exception){
            Log.i(TAG, "getRouteTariffPricesaaw:: ${e.message} ")
        }
    }




}