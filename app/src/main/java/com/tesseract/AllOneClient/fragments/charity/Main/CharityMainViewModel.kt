package com.tesseract.AllOneClient.fragments.charity.Main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.charity.index.Content
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharityMainViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val charityMain=MutableLiveData<Content>()
    val charityErrorMain=MutableLiveData<String>()


    fun charityMainGet(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.charityIndex(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        charityMain.postValue(it.body()?.content)
                    }
                }
            }
        }catch (e:Exception){
            charityErrorMain.postValue(e.message)
        }
    }


}