package com.tesseract.AllOneClient.model.home.LocationSearch

import com.google.gson.annotations.SerializedName

data class LocationSearchList(

    @field:SerializedName("address")
    var address: String? = null,

    @field:SerializedName("region")
    var region: String? = null,

    @field:SerializedName("lat")
    var lat: String? = null,

    @field:SerializedName("lon")
    var lon: String? = null



)
