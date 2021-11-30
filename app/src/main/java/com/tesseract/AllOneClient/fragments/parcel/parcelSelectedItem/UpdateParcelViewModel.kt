package com.tesseract.AllOneClient.fragments.parcel.parcelSelectedItem

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.parcel.parcelUpdate.ParcelUpdateModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class UpdateParcelViewModel @Inject constructor(private val repository: NetworkRepository) :
    ViewModel() {

    val parcelUpdateObserver = MutableLiveData<ParcelUpdateModel>()
    val updatePlaceError = MutableLiveData<String>()


    fun parcelUpdate(token: Map<String, String>, id: Int, baggagePlaces: String) =
        viewModelScope.launch {
            try {
                repository.parcelUpdate(token, id, baggagePlaces).let {
                    if (it.isSuccessful) {
                        if (it.body()?.success == true) {
                            parcelUpdateObserver.postValue(it.body())
                        } else {
                            updatePlaceError.postValue(it.body()?.message)
                        }
                    } else {
                        updatePlaceError.postValue(it.message())
                    }
                }
            } catch (e: Exception) {
                updatePlaceError.postValue(e.message)
            }
        }

    val parcelBooking = MutableLiveData<ParcelUpdateModel>()
    val parcelBookingError = MutableLiveData<String>()

    fun parcelBooking(token: Map<String, String>, orderId: Int, driverId:Int) = viewModelScope.launch {
        try {
            repository.parcelBooking(token, orderId, driverId).let {
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        parcelBooking.postValue(it.body())
                    } else {
                        parcelBookingError.postValue(it.body()?.message)
                    }
                } else {
                    parcelBookingError.postValue(it.message())
                }
            }
        } catch (e: Exception) {
            parcelBookingError.postValue(e.message)
        }
    }
}