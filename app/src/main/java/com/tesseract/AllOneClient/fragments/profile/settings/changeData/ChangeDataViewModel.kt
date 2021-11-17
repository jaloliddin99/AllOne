package com.tesseract.AllOneClient.fragments.profile.settings.changeData

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.login.UsedDetailsModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeDataViewModel @Inject constructor(private val repository: NetworkRepository) : ViewModel() {

    val text= MutableLiveData<String>()
    val userDetails= MutableLiveData<UsedDetailsModel>()

    fun update(token: Map<String, String>, fullName:String, gender: String, birthday: String)=viewModelScope.launch {
        repository.updateData(token, fullName, gender, birthday).let {

            if (it.isSuccessful){
                if (it.body()?.success==true){

                    userDetails.postValue(it.body()?.content)
                    text.postValue("registered")

                }
            }
        }
    }

}