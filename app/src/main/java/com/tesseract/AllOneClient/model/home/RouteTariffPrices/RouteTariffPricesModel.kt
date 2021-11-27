package com.tesseract.AllOneClient.model.home.RouteTariffPrices

import com.google.gson.annotations.SerializedName

data class RouteTariffPricesModel(
        @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("content")
    var content: PlacesModel?=null
)