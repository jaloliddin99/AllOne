package com.tesseract.AllOneClient.fragments.tourism.carRent.carView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.tourism.carRent.carView.CarViewModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CarViewViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val getCarView=MutableLiveData<CarViewModel>()
    val errorM=MutableLiveData<String>()

    fun getCarView(token:Map<String, String>, carId:Int)=viewModelScope.launch {
        try {
            repository.getCarView(token, carId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getCarView.postValue(it.body())
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

    val carRentRate=MutableLiveData<ClinicAddToFavouriteModel>()
    val rateError=MutableLiveData<String>()

    fun carRentAddToFavourites(token: Map<String, String>, packageId:Int)=viewModelScope.launch {
        try {
            repository.carRentAddToFavourites(token, packageId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        carRentRate.postValue(it.body())
                    }else{
                        rateError.postValue(it.message())
                    }
                }else{
                    rateError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            rateError.postValue(e.message)
        }
    }


}