package com.tesseract.AllOneClient.model.tourism.packageView

data class Content(
    val age_from: String,
    val agency_addr: String,
    val agency_closed: Boolean,
    val agency_closed_title: String,
    val agency_description: String,
    val agency_location: String,
    val agency_name: String,
    val agency_phone_number: List<String>,
    val agency_poster: String,
    val agency_telegram: String,
    val agency_website: String,
    val agency_work_time: String,
    val days: String,
    val extra_paid: List<ExtraPaid>,
    val gallery: List<String>,
    val id: Int,
    val includes: List<Include>,
    val is_favorite: Boolean,
    val name: String,
    val price_from: String,
    val route: String,
    val season: String,
    val visa: String
)