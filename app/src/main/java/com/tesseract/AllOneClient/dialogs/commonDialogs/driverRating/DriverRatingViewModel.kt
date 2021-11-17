package com.tesseract.AllOneClient.dialogs.commonDialogs.driverRating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingOptions
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPost
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPostResponse
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DriverRatingViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val driverRatingObserver=MutableLiveData<DriverRatingOptions>()
    val driverRatingPostObserver=MutableLiveData<DriverRatingPostResponse>()

    fun requestDriverRating(token: Map<String, String>, orderType: String)=viewModelScope.launch {
        try {
            repository.driverRatingOptions(token, orderType).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        driverRatingObserver.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }

    fun driverRatingPost(token: Map<String, String>, driverId: Int, body: DriverRatingPost)=viewModelScope.launch {
        try {
            repository.driverRatingPost(token, driverId, body).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        driverRatingPostObserver.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }

}