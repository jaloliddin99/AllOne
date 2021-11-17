package com.tesseract.AllOneClient.fragments.charity.DonationHistory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.charity.history.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DonationHistoryViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val data=MutableLiveData<List<Data>>()
    val errorM=MutableLiveData<String>()


    fun startMain(token: Map<String, String>, type:String, from:String, to:String) {
        historyData(token, type, from, to)
    }

    fun historyData(token: Map<String, String>, type:String, from:String, to:String)=viewModelScope.launch {
        try {
            repository.charityHistory(token, type, from, to, Common.donationCountPage).let {
                Common.donationCountPage++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        data.postValue(it.body()?.content?.data)
                    }
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }

}