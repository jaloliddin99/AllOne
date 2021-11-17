package com.tesseract.AllOneClient.model.profile.getCards

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.profile.card.StoreCardData

data class GetCardData(
    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("card_name")
    var cardName: String?=null,

    @field:SerializedName("card_color")
    var cardColor: String?=null,

    @field:SerializedName("type")
    var type: String?=null,

    @field:SerializedName("card_number")
    var cardNumber: String?=null,

    @field:SerializedName("card_validity")
    var cardValidity: String?=null,

    @field:SerializedName("balance")
    var balance: String?=null,

    @field:SerializedName("active")
    var active: Boolean?=null
)
