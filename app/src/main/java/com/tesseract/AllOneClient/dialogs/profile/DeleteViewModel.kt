package com.tesseract.AllOneClient.dialogs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val errorM=MutableLiveData<String>()
    val successM=MutableLiveData<String>()

    fun deleteCard(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.deleteCard(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        successM.postValue(it.body()?.message)
                    }else{
                        errorM.postValue(it.body()?.message)
                    }
                }else{
                    errorM.postValue("something went wrong1 "+it.message()+" "+it.code())
                }
            }

        }catch (e:Exception){

            errorM.postValue("something went wrong")
        }
    }


}