package com.tesseract.AllOneClient.model.order.getActiveOrderModel

import com.google.gson.annotations.SerializedName

data class GetActiveOrderModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: GetActiveOrderModelData?=null
)
