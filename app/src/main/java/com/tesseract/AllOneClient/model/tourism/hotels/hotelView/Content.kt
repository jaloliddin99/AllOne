package com.tesseract.AllOneClient.model.tourism.hotels.hotelView

data class Content(
    val addr: String,
    val description: String,
    val gallery: List<String>,
    val hotel_facilities: List<HotelFacility>,
    val hotel_rooms: List<HotelRoom>,
    val id: Int,
    val is_favorite: Boolean,
    val location: String,
    val name: String,
    val phone_number: List<String>,
    val poster: String,
    val rate_count: Int,
    val rating: String,
    val telegram: String,
    val website: String
)