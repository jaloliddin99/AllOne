package com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel

import com.google.gson.annotations.SerializedName

data class OrderList(

    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("order_type")
    var orderType: String?=null,

    @field:SerializedName("title")
    var title: String?=null,

    @field:SerializedName("tariff")
    var tariff: String?=null,

    @field:SerializedName("from")
    var from: String?=null,

    @field:SerializedName("to")
    var to: String?=null,

    @field:SerializedName("date")
    var date: String?=null,

    @field:SerializedName("amount")
    var amount: String?=null,

    @field:SerializedName("places")
    var places: String?=null,

    @field:SerializedName("status")
    var status: String?=null,

    @field:SerializedName("time")
    var time: String?=null,




)
