package com.tesseract.AllOneClient.fragments.taxiCity.saveUpdateLoc

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.taxiCity.deleteSavedAddress.DeleteSavedAddressModel
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateAddressBody
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateSavedLocationModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CRUDLocationViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val successM= MutableLiveData<String>()

    val deleteSavedAddressObserver=MutableLiveData<DeleteSavedAddressModel>()
    val updateSavedAddressModel=MutableLiveData<UpdateSavedLocationModel>()

    fun storeNewAddress(token: Map<String, String>, fields: Map<String, String>)=viewModelScope.launch {
        try {
            repository.postNewAddress(token, fields).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        successM.postValue(it.body()?.message)
                    }
                }
            }
        }catch (e: Exception){
            Log.i("error not reached", " error")
        }
    }

    fun updateSavedAddress(token: Map<String, String>, id: Int, updateAddressBody: UpdateAddressBody)=viewModelScope.launch { 
        try {
            repository.updateSavedAddress(token, id, updateAddressBody).let { 
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        updateSavedAddressModel.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){
            
        }
    }
    
    fun deleteSavedAddress(token: Map<String, String>, id: Int)=viewModelScope.launch { 
        try {
            repository.deleteSavedAddress(token, id).let { 
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        deleteSavedAddressObserver.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){
            
        }
    }


}