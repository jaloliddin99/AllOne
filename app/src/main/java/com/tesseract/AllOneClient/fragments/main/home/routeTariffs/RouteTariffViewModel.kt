package com.tesseract.AllOneClient.fragments.main.home.routeTariffs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.routeRariffs.RouteTariffContentListModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RouteTariffViewModel @Inject constructor(private val repository: NetworkRepository): ViewModel(){


    val routeList=MutableLiveData<List<RouteTariffContentListModel>>()

    fun getRouteTariff(token: Map<String, String>, startPoint: String, endPoint:String)=viewModelScope.launch {
        repository.getRouteTariffs(token, startPoint, endPoint).let {
            if (it.isSuccessful){
                if (it.body()?.success==true){
                    routeList.postValue(it.body()?.content)
                }
            }

        }
    }


}