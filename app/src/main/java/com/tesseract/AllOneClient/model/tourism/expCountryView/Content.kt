package com.tesseract.AllOneClient.model.tourism.expCountryView

data class Content(
    val continent: String,
    val country_name: String,
    val description: String,
    val gallery: List<String>,
    val id: Int,
    val poster: String
)