package com.tesseract.AllOneClient.fragments.order.mapActiveRegion

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import com.tesseract.AllOneClient.repository.NetworkRepositoryForRouting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MapActivityRegionViewModel @Inject constructor(private val repository: NetworkRepositoryForRouting):ViewModel() {

    val locationRouting=MutableLiveData<List<List<Double>>>()


    fun mapGetRouting(token: Map<String, String>, routingDetail: RoutindDetails)=viewModelScope.launch {
        try {
            repository.routing(token, routingDetail).let {
                if (it.isSuccessful){
                    locationRouting.postValue(it.body()?.features?.get(0)?.geometry?.coordinates)
                }
            }
        }catch (e:Exception){
        }
    }
}