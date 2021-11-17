package com.tesseract.AllOneClient.model.profile.getCards

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.profile.card.StoreCardData

data class GetCardModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: List<GetCardData>?=null
)
