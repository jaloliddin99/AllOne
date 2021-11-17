package com.tesseract.AllOneClient.model.home.postNewAddressModel

import com.google.gson.annotations.SerializedName

data class PostNewAddressModel(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("content")
    var content: String? = null
)