package com.tesseract.AllOneClient.fragments.main.home.HomeMain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.news.NewsItemModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    var responseMessage=MutableLiveData<NewsItemModel>()

    fun newsItemModel(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getNewsMain(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        responseMessage.postValue(it.body()?.getDistrictContent!!)
                    }
                }
            }
        }catch (e:Exception){

        }
    }

}