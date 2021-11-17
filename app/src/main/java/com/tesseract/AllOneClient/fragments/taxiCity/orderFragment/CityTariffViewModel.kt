package com.tesseract.AllOneClient.fragments.taxiCity.orderFragment

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import com.tesseract.AllOneClient.model.taxiCity.newOrder.CityCreateNewOrder
import com.tesseract.AllOneClient.model.taxiCity.tariffs.CityTariffMainModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CityTariffViewModel @Inject constructor(private val repository: NetworkRepository):ViewModel() {

    val cityTariffMainModelObserver=MutableLiveData<CityTariffMainModel>()
    val errorM=MutableLiveData<String>()

    fun cityTariffMainModel(token: Map<String, String>, points: Map<String, String>)=viewModelScope.launch {
        try {
            repository.cityRouteTariffs(token,points ).let {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        cityTariffMainModelObserver.postValue(it.body())
                    }
                }else{
                    errorM.postValue(it.message())
                }
            }
        }catch (e:Exception){
            errorM.postValue(e.message)
        }
    }

    val cityNewOrder=MutableLiveData<CityCreateNewOrder>()
    val error=MutableLiveData<String>()

    fun cityNewOrderPost(
        token: Map<String, String>,
        points: Map<String, String>,
        tariff: String,
        has_overhead_luggage: Int,
        has_conditioner: Int,
        for_another: Int,
        phone_number: String,
        receiver_phone_number:String,
        receiver_comment:String,
        used_bonus:Int,
        used_bonus_amount:Double,
        order_amount:Double,
        payment_type:String,
        comment:String,
        card_id:Int,
        cargo_type:String
    )=viewModelScope.launch {
        try {
            repository.cityNewOrder(token, points, tariff, has_overhead_luggage, has_conditioner, for_another, phone_number,
                receiver_phone_number, receiver_comment, used_bonus,
                used_bonus_amount, order_amount, payment_type, comment, card_id, cargo_type).let {
                    if (it.isSuccessful){
                        if (it.body()?.success==true){
                            cityNewOrder.postValue(it.body())
                        }else{
                            error.postValue(it.body()?.message)
                        }
                    }
                Log.i(TAG, "cityNewOrderPost: ${it.message()} ${it.code()}")
            }
        }catch (e:Exception){
            Log.i(TAG, "cityNewOrderPost:caca ${e.message}")
        }
    }

}