package com.tesseract.AllOneClient.fragments.medTurism.doctors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.medTourism.categories.ClinicsCategoriesModel
import com.tesseract.AllOneClient.model.medTourism.clinics.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DoctorsViewModel @Inject constructor(private val repository: NetworkRepository) :ViewModel() {

    val getDoctorsObserver = MutableLiveData<ArrayList<Data>>()

    fun startDoctors( token: Map<String, String>,
                         name: String,
                         category_id:Int,
                         city_id:Int) {
        getDoctor(token, name, category_id, city_id)
    }

    val errorMessageDoctor=MutableLiveData<String>()
    val errorClinicCategories=MutableLiveData<String>()

    fun getDoctor(
        token: Map<String, String>,
        name: String,
        category_id: Int,
        city_id: Int
    ) = viewModelScope.launch {
        try {
            repository.getDoctors(token, name, category_id, city_id, Common.doctorPaging).let {
                Common.doctorPaging++
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        getDoctorsObserver.postValue(it.body()?.content?.data as ArrayList<Data>)
                    }else{
                        errorMessageDoctor.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorMessageDoctor.postValue(it.body()?.message.toString())
                }
            }
        } catch (e: Exception) {
            errorMessageDoctor.postValue(e.message)
        }
    }

    val categories=MutableLiveData<ClinicsCategoriesModel>()
    fun getClinicCategories(token: Map<String, String>)=viewModelScope.launch {
        try {
            repository.getDoctorsCategories(token).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        categories.postValue(it.body())
                    }else{
                        errorClinicCategories.postValue(it.body()?.message.toString())
                    }
                }else{
                    errorClinicCategories.postValue(it.body()?.message.toString())
                }
            }
        }catch (e:Exception){
            errorClinicCategories.postValue(e.message)
        }
    }

}