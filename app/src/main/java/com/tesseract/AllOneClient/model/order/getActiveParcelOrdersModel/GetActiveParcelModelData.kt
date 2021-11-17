package com.tesseract.AllOneClient.model.order.getActiveParcelOrdersModel

import com.google.gson.annotations.SerializedName

data class GetActiveParcelModelData(
    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("order_type")
    var orderType: String?=null,

    @field:SerializedName("title")
    var title: String?=null,

    @field:SerializedName("tariff")
    var tariff: String?=null,
    @field:SerializedName("parcel_type")
    var parcelType: String?=null,

    @field:SerializedName("receiver")
    var receiver: String?=null,
    @field:SerializedName("driver_id")
    var driverId: Int?=null,

    @field:SerializedName("driver_name")
    var driverName: String?=null,

    @field:SerializedName("driver_avatar")
    var driverAvatar: String?=null,

    @field:SerializedName("driver_rating")
    var driverRating: Float?=null,

    @field:SerializedName("driver_phone_number")
    var driverPhoneNumber: String?=null,

    @field:SerializedName("driver_telegram")
    var driverTelegram: String?=null,

    @field:SerializedName("driver_car")
    var driverCar: String?=null,

    @field:SerializedName("driver_car_photos")
    var driverCarPhotos: List<String>?=null,

    @field:SerializedName("places")
    var placePrices: ParcelPlacePrices?=null,

    @field:SerializedName("has_overhead_luggage")
    var hasOverHeadLuggage: Boolean?=null,

    @field:SerializedName("has_conditioner")
    var hasConditioner: Boolean?=null,

    @field:SerializedName("driver_location")
    var driverLocation: String?=null,

    @field:SerializedName("from_latlng")
    var fromLatLng: String?=null,

    @field:SerializedName("to_latlng")
    var toLatLng: String?=null,

    @field:SerializedName("pickup_latlng")
    var pickUpLtLng: String?=null,

    @field:SerializedName("from")
    var from: String?=null,

    @field:SerializedName("to")
    var to: String?=null,

    @field:SerializedName("pickup")
    var pickup: String?=null,

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

    @field:SerializedName("parcel_photos")
    var parcelPhotos: List<String>?=null,

    @field:SerializedName("share_order")
    var shareOrder: String?=null,

    @field:SerializedName("order_status")
    var orderStatus: String?=null,


    )
