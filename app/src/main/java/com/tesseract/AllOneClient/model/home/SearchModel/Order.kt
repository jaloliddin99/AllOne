package com.tesseract.AllOneClient.model.home.SearchModel

data class Order(
    val id: String,
    val place_prices: PlacePrices,
    val places: String,
    val price: String,
    val tariff: String,
    val type: String
)