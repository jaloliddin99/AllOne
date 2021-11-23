package com.tesseract.AllOneClient.fragments.tourism.travelAgencies.agencyView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.tourism.agency.agencyView.TravelAgencyView
import com.tesseract.AllOneClient.model.tourism.agency.packageView.TravelPackageView
import com.tesseract.AllOneClient.model.tourism.packageView.PackageViewMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AgencyViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val agencyView=MutableLiveData<TravelAgencyView>()
    val agencyPackageView=MutableLiveData<TravelPackageView>()
    val agencyRateView=MutableLiveData<ClinicAddToFavouriteModel>()

    val errorAgency=MutableLiveData<String>()
    val errorAgencyPackage=MutableLiveData<String>()
    val errorRate=MutableLiveData<String>()


    fun agencyView(token:Map<String, String>, agencyId:Int)=viewModelScope.launch {
        try {
            repository.getAgencyView(token, agencyId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        agencyView.postValue(it.body())
                    }else{
                        errorAgency.postValue(it.message())
                    }
                }else{
                    errorAgency.postValue(it.message())
                }
            }

        }catch (e:Exception){
            errorAgency.postValue(e.message)
        }
    }

    private val sharePackage=MutableLiveData<TravelAgencyView>()
    val mutableSearchItem: LiveData<TravelAgencyView> get() = sharePackage
    fun clinicInfo(item: TravelAgencyView){
        sharePackage.value=item
    }



    fun startAgencyPackageView(token:Map<String, String>, agencyId:Int){
        agencyPackageView(token, agencyId)
    }

    fun agencyPackageView(token:Map<String, String>, agencyId:Int)=viewModelScope.launch {
        try {
            repository.getAgencyPackageView(token, agencyId, Common.agencyPackagesPager).let {
                if (it.isSuccessful){
                    Common.agencyPackagesPager++
                    if (it.body()?.success==true){
                        agencyPackageView.postValue(it.body())
                    }else{
                        errorAgencyPackage.postValue(it.message())
                    }
                }else{
                    errorAgencyPackage.postValue(it.message())
                }
            }

        }catch (e:Exception){
            errorAgencyPackage.postValue(e.message)
        }
    }

    fun agencyRateView(token:Map<String, String>, agencyId:Int)=viewModelScope.launch {
        try {
            repository.postAddToFavourites(token, agencyId).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        agencyRateView.postValue(it.body())
                    }else{
                        errorRate.postValue(it.message())
                    }
                }else{
                    errorRate.postValue(it.message())
                }
            }

        }catch (e:Exception){
            errorRate.postValue(e.message)
        }
    }

}