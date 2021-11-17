package com.tesseract.AllOneClient.model.login

import com.google.gson.annotations.SerializedName

data class DataModel(
    @field:SerializedName("token")
    var token:String?=null,

    @field:SerializedName("data")
    var data: UsedDetailsModel?=null
    )