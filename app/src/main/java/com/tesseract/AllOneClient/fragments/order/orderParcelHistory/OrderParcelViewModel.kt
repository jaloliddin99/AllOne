package com.tesseract.AllOneClient.fragments.order.orderParcelHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.getOrderParcelModel.OrderParcelATData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class OrderParcelViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val orderParcelATData=MutableLiveData<OrderParcelATData>()
    val errorMessage=MutableLiveData<String>()


    fun getParcelDeliveryOrder(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.getParcelDeliveryOrder(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        orderParcelATData.postValue(it.body()?.content)
                    }else{
                        errorMessage.postValue(it.body()?.message)
                    }
                }
            }

        }catch (e:Exception){
            errorMessage.postValue("No  internet")
        }
    }



}