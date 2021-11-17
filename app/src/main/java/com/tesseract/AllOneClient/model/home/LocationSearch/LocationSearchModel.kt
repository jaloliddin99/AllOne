package com.tesseract.AllOneClient.model.home.LocationSearch

import com.google.gson.annotations.SerializedName

data class LocationSearchModel(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("content")
    var getDistrictContent: List<LocationSearchList>? = null

)
