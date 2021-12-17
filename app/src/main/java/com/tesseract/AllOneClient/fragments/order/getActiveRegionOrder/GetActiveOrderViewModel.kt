package com.tesseract.AllOneClient.fragments.order.getActiveRegionOrder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.GetActiveOrderModelData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetActiveOrderViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {


    val getActiveOrderModelData=MutableLiveData<GetActiveOrderModelData>()
    val message=MutableLiveData<String>()

    fun taxiGetActiveOrderViewModel(token: Map<String, String>, id: Int)=viewModelScope.launch {

        try {
            repository.taxiGetActiveOrderRepo(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        if (it.body()?.content!=null){
                            getActiveOrderModelData.postValue(it.body()?.content!!)
                        }else{
                            message.postValue(it.body()?.message.toString())
                        }

                    }else{
                        message.postValue(it.message())
                    }
                }else{
                    message.postValue(it.message())
                }
            }
        }catch (e: Exception){
            message.postValue(e.message)
        }
    }



}