package com.tesseract.AllOneClient.fragments.profile.addcard.storeActivate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.profile.card.StoreCardData
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreActivateViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val errorMessage=MutableLiveData<String>()
    val postStoreCardDataResponse=MutableLiveData<StoreCardData>()

    val activateCardMsg=MutableLiveData<String>()
    val errorMessageActiveCards=MutableLiveData<String>()

    fun activateCard(
        token: Map<String, String>,
        id: Int,
        code: String
    )=viewModelScope.launch {
        try {
            repository.postActivateCard(token, id, code).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        activateCardMsg.postValue(it.body()?.message)
                    }else{
                        errorMessageActiveCards.postValue("Ok")
                    }
                }else{
                    errorMessageActiveCards.postValue("No Internt")
                }
            }

        }catch (e:Exception){
            errorMessageActiveCards.postValue("No Internt")
        }
    }

    fun storeCard(
        token: Map<String, String>,
        name: String,
        num: String,
        validity: String
    )=viewModelScope.launch {
        try {
            repository.postStoreCard(token, name, num, validity).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        postStoreCardDataResponse.postValue(it.body()?.content)
                    }else{
                        errorMessage.postValue(it.body()?.message)
                    }
                }else{
                    errorMessage.postValue("No internet1"+it.message()+" "+it.code())
                }
            }
        }catch (e:Exception){
            errorMessage.postValue("No internet")
        }
    }


}