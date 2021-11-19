package com.tesseract.AllOneClient.fragments.tourism.tourPackages

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.model.tourism.indexUzb.Data
import com.tesseract.AllOneClient.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class TourPackagesViewModel @Inject constructor(private val repository: NetworkRepository) :
    ViewModel() {

    val mainIndex = MutableLiveData<List<Data>>()
    val errorM = MutableLiveData<String>()

    fun startMainIndex(
        token: Map<String, String>,
        location: String,
        query: String,
        countryId: Int,
        currencyId: Int,
        sort: String
    ) {
        mainIndex(token, location, query, countryId, currencyId, sort)
    }

    fun mainIndex(
        token: Map<String, String>,
        location: String,
        query: String,
        countryId: Int,
        currencyId: Int,
        sort: String
    ) =
        viewModelScope.launch {
            try {
                repository.getTourIndex(
                    token,
                    location,
                    query,
                    countryId,
                    currencyId,
                    sort,
                    Common.tourIndexMain
                ).let {
                    Common.tourIndexMain++
                    if (it.isSuccessful) {
                        if (it.body()?.success == true) {
                            mainIndex.postValue(it.body()?.content?.data)

                        } else {
                            errorM.postValue(it.body()?.message.toString())
                        }
                    } else {
                        errorM.postValue(it.code().toString())
                    }
                }
            } catch (e: Exception) {
                errorM.postValue(e.message)
                Log.i(TAG, "mainIndex: ${e.message}")
            }
        }

}
