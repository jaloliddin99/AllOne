package com.tesseract.AllOneClient.model.order.getActiveParcelOrdersModel

import com.google.gson.annotations.SerializedName

data class ParcelPlacePrices(
    @field:SerializedName("1")
    val firstPlace: String,

    @field:SerializedName("2")
    val secondPlace: String,

    @field:SerializedName("3")
    val thirdPlace: String,

    @field:SerializedName("4")
    val fourthPlace: String
)
