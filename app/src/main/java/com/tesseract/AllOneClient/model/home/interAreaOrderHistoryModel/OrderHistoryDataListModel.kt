package com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel

import com.google.gson.annotations.SerializedName

data class OrderHistoryDataListModel(

    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("date")
    var date: String?=null,

    @field:SerializedName("orders")
    var orders: List<OrderList>,
)
