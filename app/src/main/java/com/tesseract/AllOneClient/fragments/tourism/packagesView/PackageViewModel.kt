package com.tesseract.AllOneClient.fragments.tourism.packagesView

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.tourism.packageView.PackageViewMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {
    val packageView=MutableLiveData<PackageViewMainModel>()

    val addToFav=MutableLiveData<ClinicAddToFavouriteModel>()
    val errorFav=MutableLiveData<String>()
    private val TAG = "PackageViewModel"

    fun addToFavourite(token: Map<String, String>, id:String)=viewModelScope.launch {
        try {
            repository.packageAddToFav(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        addToFav.postValue(it.body())
                    }else{
                        errorFav.postValue(it.body()?.message)
                    }
                }else{
                    errorFav.postValue(it.body()?.message)
                }
            }
        }catch (e:Exception){
            Log.i(TAG, "addToFavourite: ")
            errorFav.postValue(e.message)
        }
    }

    val errorM=MutableLiveData<String>()
    fun packageView(token:Map<String, String>, packageId:Int)=viewModelScope.launch {
        try {

            repository.packageView(token, packageId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        packageView.postValue(it.body())
                    }else{
                        errorM.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorM.postValue(it.body()?.message.toString())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }


    private val sharePackage=MutableLiveData<PackageViewMainModel>()
    val mutableSearchItem: LiveData<PackageViewMainModel> get() = sharePackage
    fun clinicInfo(item: PackageViewMainModel){
        sharePackage.value=item
    }
}