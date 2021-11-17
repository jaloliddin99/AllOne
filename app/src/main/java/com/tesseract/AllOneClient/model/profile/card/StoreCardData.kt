package com.tesseract.AllOneClient.model.profile.card

import com.google.gson.annotations.SerializedName

data class StoreCardData(
    @field:SerializedName("id")
    var id: Int?=null,
    @field:SerializedName("card_color")
    var cardColor: String?=null,
    @field:SerializedName("phone_number")
    var phoneNumber: String?=null
)