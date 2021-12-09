package com.tesseract.AllOneClient.model.parcel.parcelSearch

data class Order(
    val id: String,
    val price: String,
    val tariff: String,
    val tariff_type:String,
    val start_point:Int,
    val end_point:Int,
    val baggage_price:String,
    val places:String,
    val type: String
)