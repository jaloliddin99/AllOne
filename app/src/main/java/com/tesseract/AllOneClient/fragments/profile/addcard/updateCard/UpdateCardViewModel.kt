package com.tesseract.AllOneClient.fragments.profile.addcard.updateCard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateCardViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val successM=MutableLiveData<String>()
    val errorM=MutableLiveData<String>()

    fun updateCard(token: Map<String, String>, id: Int, cardName: String)=viewModelScope.launch {
        try {
            repository.updateCard(token, id, cardName).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        successM.postValue(it.body()?.message!!)
                    }else{
                        errorM.postValue(it.body()?.message!!)
                    }
                }else{
                    errorM.postValue("something went wrong")
                }
            }

        }catch (e:Exception){
            errorM.postValue("something went wrong")
        }
    }





}