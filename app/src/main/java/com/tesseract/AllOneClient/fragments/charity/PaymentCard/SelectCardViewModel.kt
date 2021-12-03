package com.tesseract.AllOneClient.fragments.charity.PaymentCard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.charity.projectCards.Content
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SelectCardViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val creditCard=MutableLiveData<Content>()
    val errorM=MutableLiveData<String>()

    fun gerCreditCards(token: Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.charityCreditCards(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        creditCard.postValue(it.body()?.content)
                    }else{
                        errorM.postValue(it.body()?.message)
                    }
                }else{
                    errorM.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }
}