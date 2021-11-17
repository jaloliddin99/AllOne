package com.tesseract.AllOneClient.model.parcel.parcelSearch

data class Order(
    val id: String,
    val place_prices: PlacePrices,
    val price: String,
    val tariff: String,
    val type: String
)