package com.tesseract.AllOneClient.fragments.tourism.exploreCountry

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.tourism.expCountryPackage.ExploreCountryPackage
import com.tesseract.AllOneClient.model.tourism.expCountryView.ExploreCountryView
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ExploreCountryViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val exploreCountryPackages=MutableLiveData<ExploreCountryPackage>()
    val packageError=MutableLiveData<String>()


    val exploreCountryView=MutableLiveData<ExploreCountryView>()
    val countryError=MutableLiveData<String>()

    fun starterExploreCP(token: Map<String, String>, id: Int){
        exploreCP(token, id)
    }

    fun exploreCP(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.exploreCountryPackages(token, id, Common.countryPageee).let {
                Common.countryPageee++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        exploreCountryPackages.postValue(it.body())
                    }else{
                        packageError.postValue(it.message())
                    }
                }else{
                    packageError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            packageError.postValue(e.message)
        }
    }

    fun exploreCV(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.exploreCountryView(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        exploreCountryView.postValue(it.body())
                    }else{
                        countryError.postValue(it.message())
                    }
                }else{
                    countryError.postValue(it.message())
                }
            }
        }catch (e:Exception){
            countryError.postValue(e.message)
        }
    }


}