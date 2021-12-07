package com.tesseract.AllOneClient.fragments.tourism.carRent.car

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.tourism.carRent.cars.CarRentCarsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val carRentCarObserver=MutableLiveData<CarRentCarsModel>()

    val errorM=MutableLiveData<String>()

    fun startCarViewModel(token:Map<String, String>, query:String, countryId:Int, currencyId:Int, sort:String,  carId:Int){
        carRentRequest(token, query, countryId, currencyId, sort,  carId)
    }

    fun carRentRequest(token:Map<String, String>, query:String, countryId:Int, currencyId:Int, sort:String,  carId:Int)
    =viewModelScope.launch {
        try {
            repository.getCarRentCar(token, query, countryId, currencyId, sort,  carId, Common.carRentPageId).let {
                if (it.isSuccessful){
                    Common.carRentPageId++
                    if (it.body()?.success==true){
                        carRentCarObserver.postValue(it.body())
                    }else{
                        errorM.postValue(it.message())
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