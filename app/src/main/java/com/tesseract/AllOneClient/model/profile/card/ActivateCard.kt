package com.tesseract.AllOneClient.model.profile.card

import com.google.gson.annotations.SerializedName

data class ActivateCard(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: String?=null
)
