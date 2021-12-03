package com.tesseract.AllOneClient.fragments.profile.addcard.getCards

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetCardViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val errorM=MutableLiveData<String>()

    val cardDataList=MutableLiveData<List<GetCardData>>()

    fun getCardDataList(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getCardData(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        cardDataList.postValue(it.body()?.content!!)
                    }else{
                        errorM.postValue("something went wrong")
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