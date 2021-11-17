package com.tesseract.AllOneClient.model.order.aboutDriverModel

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.PlacePrices

data class AboutDriverData(
    @field:SerializedName("id")
    var id: Int?=null,

    @field:SerializedName("name")
    var name: String?=null,

    @field:SerializedName("avatar")
    var avatar: String?=null,

    @field:SerializedName("rating")
    var rating: Double?=null,

    @field:SerializedName("phone_number")
    var phoneNumber: String?=null,

    @field:SerializedName("car")
    var car: String?=null,

    @field:SerializedName("car_photos")
    var carPhotos: List<String>?=null,

    )
