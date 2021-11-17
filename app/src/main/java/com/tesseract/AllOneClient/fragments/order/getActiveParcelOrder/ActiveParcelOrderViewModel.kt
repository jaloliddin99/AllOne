package com.tesseract.AllOneClient.fragments.order.getActiveParcelOrder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.getActiveParcelOrdersModel.GetActiveParcelModelData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveParcelOrderViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {


    val getActiveParcelData=MutableLiveData<GetActiveParcelModelData>()
    val errorM=MutableLiveData<Int>()

    fun getParcelActiveOrders(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {

            repository.getParcelActiveOrders(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getActiveParcelData.postValue(it.body()?.content)
                    }else{
                        errorM.postValue(1)
                    }
                }else{
                    errorM.postValue(2)
                }
            }

        }catch (e:Exception){
            errorM.postValue(3)
        }

    }


}