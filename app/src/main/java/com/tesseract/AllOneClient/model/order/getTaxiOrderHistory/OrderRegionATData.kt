package com.tesseract.AllOneClient.model.order.getTaxiOrderHistory

import com.google.gson.annotations.SerializedName

data class OrderRegionATData(
    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("order_type")
    var orderType: String?=null,
    @field:SerializedName("title")
    var title: String?=null,

    @field:SerializedName("tariff")
    var tariff: String?=null,
    @field:SerializedName("driver_id")
    var driverId: Int?=null,

    @field:SerializedName("driver_name")
    var driverName: String?=null,
    @field:SerializedName("from_latlng")
    var fromLatLng: String?=null,

    @field:SerializedName("to_latlng")
    var toLatLng: String?=null,
    @field:SerializedName("from")
    var from: String?=null,

    @field:SerializedName("to")
    var to: String?=null,

    @field:SerializedName("date")
    var date: String?=null,

    @field:SerializedName("distance")
    var distance: String?=null,

    @field:SerializedName("time")
    var time: String?=null,

    @field:SerializedName("payment_type")
    var paymentType: String?=null,

    @field:SerializedName("amount")
    var amount: String?=null,
    @field:SerializedName("bonus_amount")
    var bonusAmount: String?=null,
)
