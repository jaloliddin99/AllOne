package com.tesseract.AllOneClient.model.order.aboutDriverModel

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.GetActiveOrderModelData

data class AboutDriverModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: AboutDriverData?=null
)
