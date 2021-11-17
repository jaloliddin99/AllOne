package com.tesseract.AllOneClient.model.home.RouteTariffPrices

import com.google.gson.annotations.SerializedName

data class RouteTariffPlaceListModel(
    @field:SerializedName("place")
    var place: Int?=null,

    @field:SerializedName("price")
    var price: String?=null,

    @field:SerializedName("free")
    var free: Boolean?=null

)