package com.tesseract.AllOneClient.fragments.charity.MakeDonation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.charity.projects.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val data=MutableLiveData<List<Data>>()
    val errorM=MutableLiveData<String>()

    fun startMain(token: Map<String, String>) {
        getData(token)
    }

    fun getData(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.charityProjects(token, Common.donationProjects).let {
                Common.donationProjects++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        data.postValue(it.body()?.content?.data)
                    }
                }
            }


        }catch (e:Exception){

        }
    }

}