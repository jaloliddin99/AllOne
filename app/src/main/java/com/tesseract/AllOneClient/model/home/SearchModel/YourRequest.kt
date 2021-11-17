package com.tesseract.AllOneClient.model.home.SearchModel

data class YourRequest(
    val baggage: String,
    val driver_avatar: String,
    val driver_car: String,
    val driver_car_photos: List<String>,
    val driver_last_location: String,
    val driver_location: String,
    val driver_name: String,
    val driver_rating: Double,
    val free_places: String,
    val has_conditioner: Boolean,
    val has_luggage: Boolean,
    val id: Int
)