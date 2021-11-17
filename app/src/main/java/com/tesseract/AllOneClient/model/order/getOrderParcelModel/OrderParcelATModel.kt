package com.tesseract.AllOneClient.model.order.getOrderParcelModel

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.order.getTaxiOrderHistory.OrderRegionATData

data class OrderParcelATModel(
    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("message")
    var message: String?=null,

    @field:SerializedName("content")
    var content: OrderParcelATData?=null
)
