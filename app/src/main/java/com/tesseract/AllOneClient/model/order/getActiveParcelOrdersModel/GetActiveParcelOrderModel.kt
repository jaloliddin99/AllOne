package com.tesseract.AllOneClient.model.order.getActiveParcelOrdersModel

import com.google.gson.annotations.SerializedName

data class GetActiveParcelOrderModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: GetActiveParcelModelData?=null
)
