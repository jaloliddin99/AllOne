package com.tesseract.AllOneClient.fragments.main.home.routeTariffs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tesseract.AllOneClient.model.home.routeRariffs.ShareModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor():ViewModel() {

    private val mutableSelectedItem = MutableLiveData<ShareModel>()
    val selectedItem: LiveData<ShareModel> get() = mutableSelectedItem

    fun selectItem(item: ShareModel) {
        mutableSelectedItem.value = item
    }

}