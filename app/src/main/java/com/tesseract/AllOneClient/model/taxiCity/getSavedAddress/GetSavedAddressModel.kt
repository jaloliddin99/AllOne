package com.tesseract.AllOneClient.model.taxiCity.getSavedAddress

import com.google.gson.annotations.SerializedName

data class GetSavedAddressModel(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("content")
    var getDistrictContent: List<SavedLocationData>? = null
)
