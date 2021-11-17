package com.tesseract.AllOneClient.fragments.main.home.payments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tesseract.AllOneClient.model.home.SearchModel.Content
import com.tesseract.AllOneClient.model.home.getRegions.ShareRegionDistrict
import com.tesseract.AllOneClient.model.home.payments.ShareParcelModel
import com.tesseract.AllOneClient.model.home.payments.ShareRegionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareDataViewModel @Inject constructor(): ViewModel() {

    private val mutableSelectedItem = MutableLiveData<ShareRegionModel>()
    val selectedItem: LiveData<ShareRegionModel> get() = mutableSelectedItem
    fun selectItem(item: ShareRegionModel) {
        mutableSelectedItem.value = item
    }


    private val mutableSearchRegion=MutableLiveData<Content>()
    val mutableSearchItem:LiveData<Content> get() = mutableSearchRegion
    fun searchOrder(item:Content){
        mutableSearchRegion.value=item
    }

    private val mutableParcelSelectedItem=MutableLiveData<ShareParcelModel>()
    val selectedParcelItem: LiveData<ShareParcelModel> get() = mutableParcelSelectedItem
    fun selectedParcelItem(item: ShareParcelModel){
        mutableParcelSelectedItem.value=item
    }


    private val mutableParcelSearchModel=MutableLiveData<com.tesseract.AllOneClient.model.parcel.parcelSearch.Content>()
    val parcelItem:LiveData<com.tesseract.AllOneClient.model.parcel.parcelSearch.Content> get() = mutableParcelSearchModel
    fun parcelSearchItem(item:com.tesseract.AllOneClient.model.parcel.parcelSearch.Content){
        mutableParcelSearchModel.value=item
    }


    private val mutableShareRegionDistrict=MutableLiveData<ShareRegionDistrict>()
    val shareRegionDistrict:LiveData<ShareRegionDistrict> get() = mutableShareRegionDistrict
    fun selectShareRegionDistrict(item: ShareRegionDistrict){
        mutableShareRegionDistrict.value=item
    }





}