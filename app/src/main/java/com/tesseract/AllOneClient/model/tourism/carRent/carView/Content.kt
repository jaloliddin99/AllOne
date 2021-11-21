package com.tesseract.AllOneClient.model.tourism.carRent.carView

data class Content(
    val color: String,
    val company_addr: String,
    val company_closed: Boolean,
    val company_closed_title: String,
    val company_location: String,
    val company_name: String,
    val company_phone_number: List<String>,
    val company_poster: String,
    val company_rent_terms: String,
    val company_telegram: String,
    val company_website: String,
    val company_work_time: String,
    val engine_volume: String,
    val fuel_type: String,
    val has_conditioner: String,
    val id: Int,
    val insurance: String,
    val is_favorite: Boolean,
    val logo: String,
    val mortgage_price: String,
    val name: String,
    val poster: String,
    val price: String,
    val transmisson: String
)