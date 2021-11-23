package com.tesseract.AllOneClient.model.tourism.agency.agencyView

data class Content(
    val addr: String,
    val closed: Boolean,
    val closed_title: String,
    val description: String,
    val gallery: List<String>,
    val id: Int,
    val is_favorite: Boolean,
    val location: String,
    val name: String,
    val phone_number: List<String>,
    val poster: String,
    val rate_count: Int,
    val rating: String,
    val telegram: String,
    val website: String,
    val work_time: String
)