package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicsViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {
    val clinicMain=MutableLiveData<ClinicMainModel>()

    val addTOFavourite=MutableLiveData<ClinicAddToFavouriteModel>()
    val ratingListener=MutableLiveData<ClinicAddToFavouriteModel>()

    val errorFav=MutableLiveData<String>()
    val errorRating= MutableLiveData<String>()


    fun ratingObserver(token: Map<String, String>, name: String, id: Int, rating:Int, comment: String)=viewModelScope.launch {
        try {
            repository.medTourismClinicRate(token,name, id, rating, comment).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        ratingListener.postValue(it.body())
                    }else{
                        errorRating.postValue(it.body()?.message)
                    }
                }else{
                    errorRating.postValue(it.body()?.message)
                }
            }
        }catch (e:Exception){
            errorRating.postValue(e.message)
        }
    }

    fun addToFavouriteModel(token: Map<String, String>, name:String, id: Int)=viewModelScope.launch {
        try {
            repository.clinicAddToFavourite(token,name,  id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        addTOFavourite.postValue(it.body())
                    }else{
                        errorFav.postValue(it.body()?.message)
                    }
                }else{
                    errorFav.postValue(it.body()?.message)
                }
            }
        }catch (e:Exception){
            errorFav.postValue(e.message)
        }
    }

    val errorAction=MutableLiveData<String>()
    fun clinicMainModel(token:Map<String, String>, id:String)=viewModelScope.launch {
        try {
            repository.getClinicView(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        clinicMain.postValue(it.body())
                    }else{
                        errorAction.postValue(it.message()+" ${it.code()}")
                    }
                }else{
                    errorAction.postValue(it.message()+" ${it.code()}")
                }
            }
        }catch (e:Exception){
            errorAction.postValue(e.message)
        }
    }





    private val shareClinicData=MutableLiveData<ClinicMainModel>()
    val mutableSearchItem: LiveData<ClinicMainModel> get() = shareClinicData
    fun clinicInfo(item: ClinicMainModel){
        shareClinicData.value=item
    }

}