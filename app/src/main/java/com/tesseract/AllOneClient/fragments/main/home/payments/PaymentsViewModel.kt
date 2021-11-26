package com.tesseract.AllOneClient.fragments.main.home.payments

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.model.home.nowOrder.NewOrderInterAreaModel
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(private val repository: NetworkRepository) :
    ViewModel() {


    val newOrder = MutableLiveData<NewOrderInterAreaModel>()
    val newOrderError = MutableLiveData<String>()


    val parcelNewOrderObserver = MutableLiveData<NewOrderInterAreaModel>()


    fun parcelNewOrder(
        token: Map<String, String>,
        startPoint: Int,
        endPoint: Int,
        depDate: String,
        depTime:String,
        location: String,
        receiverName: String,
        receiverPhone:String,
        baggage: String,
        baggagePlaces: String,
        paymentType: String,
        usedBonus: Boolean,
        usedBonusAmount: Double,
        orderAmount: Double,
        details: Map<String, String>,
        hasOverheadLuggage:Boolean,
        forAnother: Boolean,
        phoneNumber: String,
        comment: String,
        cardId: Int
    ) = viewModelScope.launch {
        try {
            repository.parcelNewOrder(
                token, startPoint, endPoint, depDate,depTime, location, receiverName,
                receiverPhone, baggage, baggagePlaces, paymentType, usedBonus, usedBonusAmount, orderAmount, details
            , hasOverheadLuggage, forAnother, phoneNumber, comment, cardId).let {
                if (it.isSuccessful) {
                    if (it.body()?.success == true) {
                        parcelNewOrderObserver.postValue(it.body())
                    }
                }
                Log.i(TAG, "parcelNewOrder: ${it.body()?.message} ${it.code()} ${it.message()}")
            }
        }catch (e:Exception){

        }
    }



    fun interAreaNewOrder(
        token: Map<String, String>,
        startPoint: Int,
        endPoint: Int,
        tariff: String,
        passangerCount: Int,
        places: String,
        depDate: String,
        location: String,
        baggage: String,
        baggagePlaces: String,
        hasLuggage: Int,
        hasConditioner: Int,
        forAnother: Int,
        phoneNumber: String,
        paymentType: String,
        usedBosus: Int,
        usedAmount: Double,
        orderAmount: Double,
        comment: String,
        cardId:Int
    )
            =viewModelScope.launch {
        repository.newOrderInter(
            token, startPoint, endPoint, tariff, passangerCount, places, depDate,location, baggage, baggagePlaces, hasLuggage, hasConditioner,
            forAnother, phoneNumber, paymentType, usedBosus, usedAmount, orderAmount,comment, cardId
        ).let {
            try {
                if (it.isSuccessful){
                    if (it.body()?.success==true){
                        newOrder.postValue(it.body())
                        Log.i(TAG, "newOrder:awdqqqq ${it.message()}  ${it.code()}")
                    }
                }

                Log.i(TAG, "newOrder:wdawd ${it.message()}  ${it.code()}")
            }catch (e:Exception){
                Log.i(TAG, "newOrder:errer ${e.message}")
                newOrderError.postValue(e.message)
            }

        }
    }

}