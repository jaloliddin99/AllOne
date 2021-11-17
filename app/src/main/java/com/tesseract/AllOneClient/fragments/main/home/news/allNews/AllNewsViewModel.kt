package com.tesseract.AllOneClient.fragments.main.home.news.allNews

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.home.news.allNews.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AllNewsViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val data=MutableLiveData<List<Data>>()

    fun getAllData(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getAllNews(token,  Common.allNewsCounter).let {
                Common.allNewsCounter++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        data.postValue(it.body()?.content?.data)
                    }
                }
                Log.i("msg ", "happened"+it.code()+it.message())
            }
        }catch (e:Exception){
            Log.i("error ", "happened")
        }
    }

    fun start(token: Map<String, String>) {
        getAllData(token)
    }

}