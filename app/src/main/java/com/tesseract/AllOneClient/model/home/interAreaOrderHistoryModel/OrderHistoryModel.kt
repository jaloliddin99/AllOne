package com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel

import com.google.gson.annotations.SerializedName

data class OrderHistoryModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content:  OrderHistoryContent?=null
)