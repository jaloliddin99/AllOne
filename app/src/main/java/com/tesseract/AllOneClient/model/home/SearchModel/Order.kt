package com.tesseract.AllOneClient.model.home.SearchModel

data class Order(
    val baggage_price: String,
    val end_point: Int,
    val id: Int,
    val places: String,
    val price: String,
    val start_point: Int,
    val tariff: String,
    val tariff_type: String,
    val type: String
)