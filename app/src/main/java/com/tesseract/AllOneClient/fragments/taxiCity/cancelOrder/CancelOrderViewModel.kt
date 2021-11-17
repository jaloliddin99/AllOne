package com.tesseract.AllOneClient.fragments.taxiCity.cancelOrder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.taxiCity.CancelOrder.CancelOrder
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelBody
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelOrderPost
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CancelOrderViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val cancelOrder=MutableLiveData<CancelOrder>()
    val cancelOrderPost= MutableLiveData<CancelOrderPost>()

    fun getCancelOrderOptions(token: Map<String, String>, type: String)=viewModelScope.launch {
        try {
            repository.cancelOrder(token, type).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        cancelOrder.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }
    fun cancelOrderPostData(token: Map<String, String>, type: String, id: Int, cancelBody:CancelBody)=viewModelScope.launch {
        try {
            repository.cancelOrderPost(token, type, id,cancelBody).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        cancelOrderPost.postValue(it.body())
                    }
                }
                Log.i("error happaneddwadawd ", ""+it.code()+ it.message()+it.body()?.message)
            }

        }catch (e:Exception){
            Log.i("error happaned ", ""+e.message)
        }
    }

}