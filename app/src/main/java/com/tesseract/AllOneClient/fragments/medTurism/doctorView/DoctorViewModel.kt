package com.tesseract.AllOneClient.fragments.medTurism.doctorView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.medTourism.doctorView.DoctorViewMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class DoctorViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {
    val doctorViewMainModel=MutableLiveData<DoctorViewMainModel>()

    fun doctorViewMainModel(token:Map<String, String>, id:Int)=viewModelScope.launch {
        try {
            repository.getDoctorView(token, id).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        doctorViewMainModel.postValue(it.body())
                    }
                }
            }
        }catch (e:Exception){

        }
    }
}