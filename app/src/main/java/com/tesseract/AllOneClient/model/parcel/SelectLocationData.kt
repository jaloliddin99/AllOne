package com.tesseract.AllOneClient.model.parcel

import com.google.gson.annotations.SerializedName

data class SelectLocationData(
    @field:SerializedName("address")
    var address: String?=null,

    @field:SerializedName("region")
    var region: String?=null
)
