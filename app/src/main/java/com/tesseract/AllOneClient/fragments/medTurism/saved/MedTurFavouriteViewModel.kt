package com.tesseract.AllOneClient.fragments.medTurism.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.medTourism.favourites.FavouritesModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MedTurFavouriteViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel(){

    val favouritesObserver=MutableLiveData<FavouritesModel>()

    val deleteFromFavourites= MutableLiveData<ClinicAddToFavouriteModel>()

    val errorFAV= MutableLiveData<String>()
    val errorDelet= MutableLiveData<String>()

    fun delete(token: Map<String, String>,medOrTour:String, pageId:Int, type: String)=viewModelScope.launch {
        try {
            repository.deleteFromFavourites(token, medOrTour, pageId, type).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        deleteFromFavourites.postValue(it.body())
                    }
                }else{
                    errorDelet.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorDelet.postValue(e.message)
        }
    }

    fun starterFun(token: Map<String, String>, medOrTour:String){
        favourites(token, medOrTour)
    }

    fun favourites(token:Map<String, String>,  medOrTour:String)=viewModelScope.launch {
        try {
            repository.getFavourites(token, medOrTour,  Common.favouritesPagingApi).let{
                Common.favouritesPagingApi++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        favouritesObserver.postValue(it.body())
                    }
                }else{
                    errorFAV.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorFAV.postValue(e.message)
        }
    }

}