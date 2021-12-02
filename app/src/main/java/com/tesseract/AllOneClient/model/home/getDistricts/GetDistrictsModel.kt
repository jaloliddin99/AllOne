package com.tesseract.AllOneClient.model.home.getDistricts

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.home.getRegions.GetRegionDetails

data class GetDistrictsModel(
    @field:SerializedName("success")
    var success: Boolean? = null,
    val message:Any?=null,
    @field:SerializedName("content")
    var getDistrictContent: DistrictContent? = null
)