package com.tesseract.AllOneClient.fragments.tourism.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.tourism.explore.TourExploreModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val exploreObserver=MutableLiveData<TourExploreModel>()

    val errorM=MutableLiveData<String>()

    fun explore(token:Map<String, String>)=viewModelScope.launch {
        try {
            repository.getTourExplore(token).let{
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        exploreObserver.postValue(it.body())
                    }else{
                        errorM.postValue(it.message())
                    }
                }else{
                    errorM.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }

}