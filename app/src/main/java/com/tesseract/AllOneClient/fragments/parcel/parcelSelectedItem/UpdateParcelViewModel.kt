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
class UpdateParcelViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val parcelUpdateObserver=MutableLiveData<ParcelUpdateModel>()


    fun parcelUpdate(token: Map<String, String>, id: Int, baggagePlaces:String)=viewModelScope.launch {
        try {
            repository.parcelUpdate(token, id,baggagePlaces).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        parcelUpdateObserver.postValue(it.body())
                    }
                }
                Log.i(TAG, "parcelUpdate: ${it.message()} ${it.code()}")
            }
        }catch (e:Exception){
            Log.i("vroifeofie", ""+e.message)
        }
    }
}