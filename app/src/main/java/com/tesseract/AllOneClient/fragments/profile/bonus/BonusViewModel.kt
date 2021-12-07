package com.tesseract.AllOneClient.fragments.profile.bonus

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.profile.bonus.BonusMainModel
import com.tesseract.AllOneClient.model.profile.bonus.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class BonusViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

     val getAllBonuses=MutableLiveData<List<Data>>()
     val errorM=MutableLiveData<String>()

    fun startBonus(token: Map<String, String>){
        getAllBonuses(token)
    }

    fun getAllBonuses(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getAllBonuses(token, Common.getAllPonusesPage).let {
                if (it.isSuccessful){
                    Common.getAllPonusesPage++
                    if (it.body()?.success==true){
                        getAllBonuses.postValue(it.body()?.content?.data)
                    }else{
                        errorM.postValue(it.body()?.message?.toString())
                    }
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }


}