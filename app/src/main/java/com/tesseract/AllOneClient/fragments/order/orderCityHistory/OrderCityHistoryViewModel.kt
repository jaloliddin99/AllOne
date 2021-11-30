package com.tesseract.AllOneClient.fragments.order.orderCityHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.cityOrderHistory.CityOrderHistoryModel
import com.tesseract.AllOneClient.model.order.cityOrderHistory.Content
import com.tesseract.AllOneClient.model.order.getOrderParcelModel.OrderParcelATData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class OrderCityHistoryViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val getOrderCityHistory= MutableLiveData<Content>()
    val errorMessage= MutableLiveData<String>()


    fun getOrderCityHistory(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.getOrderCityHistory(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getOrderCityHistory.postValue(it.body()?.content)
                    }else{
                        errorMessage.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorMessage.postValue(it.message())
                }
            }

        }catch (e: Exception){
            errorMessage.postValue("No  internet")
        }
    }


}