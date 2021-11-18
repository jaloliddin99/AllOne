package com.tesseract.AllOneClient.fragments.medTurism.clinics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.medTourism.categories.ClinicsCategoriesModel
import com.tesseract.AllOneClient.model.medTourism.categories.Content
import com.tesseract.AllOneClient.model.medTourism.clinics.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicsViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {
    val clinicLists=MutableLiveData<ArrayList<Data>>()

    fun startGetClinics( token: Map<String, String>,
                         name: String,
                         category_id:Int,
                         city_id:Int) {
        getClinics(token, name, category_id, city_id)
    }

    val errorMessageClinic=MutableLiveData<String>()
    val errorMCategory=MutableLiveData<String>()

    fun getClinics(
        token: Map<String, String>,
        name: String,
        category_id:Int,
        city_id:Int
    )=viewModelScope.launch {
        try {
            repository.clinicsList(token, name, category_id, city_id, Common.clinicsPaging).let {
                Common.clinicsPaging++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        clinicLists.postValue(it.body()?.content?.data as ArrayList<Data>)
                    }else{
                        errorMessageClinic.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorMessageClinic.postValue(it.body()?.message.toString())
                }
            }
        }catch (e:Exception){
            errorMessageClinic.postValue(e.message)
        }
    }

    val categories=MutableLiveData<ClinicsCategoriesModel>()
    fun getClinicCategories(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getClinicsCategories(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        categories.postValue(it.body())
                    }else{
                        errorMCategory.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorMCategory.postValue(it.body()?.message.toString())
                }
            }
        }catch (e:Exception){
            errorMCategory.postValue(e.message)
        }
    }

}