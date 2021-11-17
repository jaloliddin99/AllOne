package com.tesseract.AllOneClient.fragments.charity.ChooseYourCard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.charity.donate.CharityDonate
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DonateViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val donateSuccess=MutableLiveData<CharityDonate>()

    fun donate(token: Map<String, String>, id:Int, client_card_id:Int, charity_project_card_id:Int, amount:Double)=viewModelScope.launch {
        try {
            repository.charityDOnate(token, id, client_card_id, charity_project_card_id, amount).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        donateSuccess.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }
}