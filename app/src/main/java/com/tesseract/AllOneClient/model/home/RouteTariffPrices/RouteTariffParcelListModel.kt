package com.tesseract.AllOneClient.model.home.RouteTariffPrices

import com.google.gson.annotations.SerializedName

data class RouteTariffParcelListModel (
    @field:SerializedName("parcel")
    var parcel: String?=null,

    @field:SerializedName("price")
    var price: String?=null,

    @field:SerializedName("free")
    var free: Boolean?=null
        )