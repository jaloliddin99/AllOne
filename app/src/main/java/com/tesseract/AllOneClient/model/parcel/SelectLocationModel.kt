package com.tesseract.AllOneClient.model.parcel

import com.google.gson.annotations.SerializedName

data class SelectLocationModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: SelectLocationData?=null

)
