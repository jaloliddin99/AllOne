package com.tesseract.AllOneClient.model.profile

import com.google.gson.annotations.SerializedName

data class SendCode(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("content")
    var content: String?=null
)
