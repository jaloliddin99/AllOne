package com.tesseract.AllOneClient.fragments.order.orderHome

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {

    val orderHistory = MutableLiveData<OrderHistoryModel>()
    val activeOrders = MutableLiveData<OrderHistoryModel>()
    val activeOrdersMain = MutableLiveData<OrderHistoryModel>()

    fun historyList(token: Map<String, String>, from: String, to: String) = viewModelScope.launch {
        try {
            repository.getInterAreaOrderHistory(token, from, to, Common.orderHistoryCountPage).let {
                Common.orderHistoryCountPage++
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        orderHistory.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }

    }


    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    var loading = MutableLiveData<Boolean>()
    var getNetworkError = MutableLiveData<String>()

    fun start(token: Map<String, String>) {
        activeNext(token)
    }

    fun startMain(token: Map<String, String>) {
        activeNextMain(token)
    }

    fun startOrderHistory(token: Map<String, String>, from: String, to: String) {
        historyList(token, from, to)
    }

    fun activeNextMain(token: Map<String, String>) = viewModelScope.launch {
        loading.value = true
        try{
            repository.getActiveOrders(token, Common.countPage).let {
                Common.countPageMain++
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        activeOrdersMain.postValue(it.body())
                    }
                }
            }
        }catch(e:Exception){

        }

    }

    fun clear(){
        Log.i("active orders", "is null")
        activeOrders.value=null
    }

    fun activeNext(token: Map<String, String>) = viewModelScope.launch {
        loading.value = true
        try {
            repository.getActiveOrders(token, Common.countPage).let {
                Common.countPage++
                Log.i("current number ", ""+Common.countPage)
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        activeOrders.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }

    }

    private fun onError(message: String) {
        getNetworkError.postValue(message)
        loading.postValue(false)
    }

}