package com.tesseract.AllOneClient.dialogs.commonDialogs.Complaints

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.dialogComplaint.Content
import com.tesseract.AllOneClient.model.dialogComplaint.MakeComplaintDriverResponseModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogComplaintViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val complaintOptions=MutableLiveData<List<Content>>()
    val makeComplaintResponse=MutableLiveData<MakeComplaintDriverResponseModel>()

    fun complaint(token: Map<String, String>, type:String)=viewModelScope.launch {
        try {
            repository.complaintToDriver(token, type).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        complaintOptions.postValue(it.body()?.content)
                    }
                }
            }
        }catch (e:Exception){

        }
    }

    fun makeComplaintPost(token: Map<String, String>,  id:Int, driverId:Int, reason:String, comment:String)=viewModelScope.launch {
        try {
            repository.makeComplaintToDriver(token, id,  driverId, reason, comment).let {
                if (it.isSuccessful){
                    makeComplaintResponse.postValue(it.body())
                }
                Log.i(TAG, "makeComplaintPost: ${it.body()?.message} ${it.body()?.success}")
                Log.i(TAG, "makeComplaintPost: ${it.message()} ${it.code()}")
            }
        }catch (e:Exception){
            Log.i("TAG", "makeComplaintPost: ${e.message}")
        }
    }

}