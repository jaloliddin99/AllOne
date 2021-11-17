package com.tesseract.AllOneClient.model.profile.updateDelete

import com.google.gson.annotations.SerializedName

data class UpdateDeleteModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: String?=null,
)
