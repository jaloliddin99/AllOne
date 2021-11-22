package com.tesseract.AllOneClient.fragments.tourism.hotels.hoterView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicMainModel
import com.tesseract.AllOneClient.model.tourism.hotels.hotelView.TourHotelView
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HotelViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    var hotelModel=MutableLiveData<TourHotelView>()

    val errorM=MutableLiveData<String>()

    fun hotelView(token: Map<String, String>, hotelId:Int)=viewModelScope.launch {
        try {
            repository.getHotelView(token, hotelId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        hotelModel.postValue(it.body())
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

    val hotelAddToFav=MutableLiveData<ClinicAddToFavouriteModel>()
    val hotelError=MutableLiveData<String>()

    fun hotelAddToFav(token: Map<String, String>, hotelId: Int)=viewModelScope.launch {
        try {
            repository.hotelAddToFav(token, hotelId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        hotelAddToFav.postValue(it.body())
                    }else{
                        hotelError.postValue(it.message())
                    }
                }else{
                    hotelError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            hotelError.postValue(e.message)
        }
    }


    private val shareClinicData=MutableLiveData<TourHotelView>()
    val mutableSearchItem: LiveData<TourHotelView> get() = shareClinicData
    fun clinicInfo(item: TourHotelView){
        shareClinicData.value=item
    }


}