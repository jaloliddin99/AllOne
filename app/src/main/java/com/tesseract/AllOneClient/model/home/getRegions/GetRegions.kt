package com.tesseract.AllOneClient.model.home.getRegions

import com.google.gson.annotations.SerializedName

data class GetRegions(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message:String?=null,

    @field:SerializedName("content")
    var getRegionList: List<GetRegionDetails>? = null
)