package com.tesseract.AllOneClient.fragments.order.mapActiveCity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.activeOrderCity.Content
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CityActiveViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val cityActiveOrderModel=MutableLiveData<Content>()
    val errorM=MutableLiveData<String>()

    fun cityActiveOrderModel(token:Map<String, String>, orderId:Int)=viewModelScope.launch {
        try {
            repository.getCityActiveOrders(token, orderId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        cityActiveOrderModel.postValue(it.body()?.content)
                    }else{
                        errorM.postValue(it.body()?.message.toString())
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