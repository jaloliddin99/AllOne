package com.tesseract.AllOneClient.fragments.medTurism.clinicInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ClinicsViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {
    val clinicMain=MutableLiveData<ClinicMainModel>()

    val addTOFavourite=MutableLiveData<ClinicAddToFavouriteModel>()

    fun addToFavouriteModel(token: Map<String, String>, id: Int)=viewModelScope.launch {
        try {
            repository.clinicAddToFavourite(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        addTOFavourite.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }


    fun clinicMainModel(token:Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.getClinicView(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        clinicMain.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }





    private val shareClinicData=MutableLiveData<ClinicMainModel>()
    val mutableSearchItem: LiveData<ClinicMainModel> get() = shareClinicData
    fun clinicInfo(item: ClinicMainModel){
        shareClinicData.value=item
    }

}