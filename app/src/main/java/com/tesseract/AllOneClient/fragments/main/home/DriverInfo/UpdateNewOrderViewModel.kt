package com.tesseract.AllOneClient.fragments.main.home.DriverInfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.updateNewOrder.NewOrderUpdateModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateNewOrderViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val updateNewOrder=MutableLiveData<NewOrderUpdateModel>()
    val errorM=MutableLiveData<String>()

    fun update(token: Map<String, String>, id: Int, passengerCount:Int, places: String)=viewModelScope.launch {
        try {
            repository.newOrderUpdate(token, id, passengerCount, places).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        updateNewOrder.postValue(it.body())
                    }else{
                        errorM.postValue(it.body()?.message)
                    }
                }
            }
        }catch (e:Exception){

        }
    }

    val interAreaBooking=MutableLiveData<ClinicAddToFavouriteModel>()
    val interAreaError=MutableLiveData<String>()


    fun interAreaBooking(token: Map<String, String>, orderId:Int, driverId:Int)=viewModelScope.launch {
        try {
            repository.interAreaBooking(token, orderId, driverId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        interAreaBooking.postValue(it.body())
                    }else{
                        interAreaError.postValue(it.body()?.message)
                    }
                }else{
                    interAreaError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            interAreaError.postValue(e.message)
        }
    }


}