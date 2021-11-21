package com.tesseract.AllOneClient.fragments.tourism.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.tourism.countries.TourismCountries
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TourismFilterViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val getAllCountries=MutableLiveData<TourismCountries>()
    val countriesError=MutableLiveData<String>()

    val getAllCurrencies=MutableLiveData<TourismCountries>()
    val currencyError=MutableLiveData<String>()


    fun getAllCountries(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getAllCountries(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getAllCountries.postValue(it.body())
                    }else{
                        countriesError.postValue(it.message())
                    }
                }else{
                    countriesError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            countriesError.postValue(e.message)
        }
    }

    fun getAllCurrencies(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getAllCurrencies(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getAllCurrencies.postValue(it.body())
                    }else{
                        currencyError.postValue(it.message())
                    }
                }else{
                    currencyError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            currencyError.postValue(e.message)
        }
    }

}