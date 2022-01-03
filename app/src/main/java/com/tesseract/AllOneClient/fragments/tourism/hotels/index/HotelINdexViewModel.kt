package com.tesseract.AllOneClient.fragments.tourism.hotels.index

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.tourism.hotels.index.HotelIndex
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HotelINdexViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val errorM=MutableLiveData<String>()
    val hotelIndex=MutableLiveData<HotelIndex>()

    fun startHotelIndex(token:Map<String, String>, tourismOrMed:String,hotelOrSan:String, query:String, countryId:Int, currencyId:Int, sort:String){
        hotelIndex(token, tourismOrMed, hotelOrSan, query, countryId, currencyId, sort)
    }

    fun hotelIndex(token:Map<String, String>, tourismOrMed:String,hotelOrSan:String, query:String, countryId:Int, currencyId:Int, sort:String)=viewModelScope.launch {
        try {
            repository.getHotelIndex(token,tourismOrMed, hotelOrSan, query, countryId, currencyId, sort, Common.hotelIndexPager).let {
                if (it.isSuccessful){
                    Common.hotelIndexPager++
                    if (it.body()?.success==true){
                        hotelIndex.postValue(it.body())
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