package com.tesseract.AllOneClient.model.tourism.hotels.roomView

data class Content(
    val about_room: String,
    val description: String,
    val gallery: List<String>,
    val hotel_facilities: List<HotelFacility>,
    val id: Int,
    val name: String,
    val price: String
)