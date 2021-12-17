package com.tesseract.AllOneClient.fragments.parcel.parcelMain

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffParcelListModel
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffPlaceListModel
import com.tesseract.AllOneClient.model.home.nowOrder.NewOrderInterAreaModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PostServiceSelectionViewModel @Inject constructor(private val repository: NetworkRepository) :
    ViewModel() {

    val parcelList = MutableLiveData<List<RouteTariffParcelListModel>>()
    val placeList = MutableLiveData<List<RouteTariffPlaceListModel>>()
    val parcelError=MutableLiveData<String>()

    fun getRouteTariffPrices(
        token: Map<String, String>,
        orderType: String,
        startPoint: String,
        endPoint: String
    ) = viewModelScope.launch {
        try {
            repository.getParcelRouteTariffPrices(token, orderType, startPoint, endPoint).let {
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        parcelList.postValue(it.body()?.content?.parcels!!)
                        placeList.postValue(it.body()?.content?.places!!)
                    }else{
                        parcelError.postValue(it.message())
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

