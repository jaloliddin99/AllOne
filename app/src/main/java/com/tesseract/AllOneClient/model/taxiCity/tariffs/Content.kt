package com.tesseract.AllOneClient.model.taxiCity.tariffs

data class Content(
    val ac: Any,
    val arrival_time: String,
    val cars: String,
    val high_demand: Boolean,
    val icon: String,
    val id: Int,
    val img: String,
    val info: String,
    val opt: List<Opt>,
    val price: String,
    val price_with_ac: String,
    val start_price: String,
    val tariff: String,
    val title: String
)