package com.tesseract.AllOneClient.model.medTourism.clinicServices

data class Content(
    val addr: String,
    val closed: Boolean,
    val closed_title: String,
    val description: String,
    val doctors: List<Doctor>,
    val gallery: List<String>,
    val id: Int,
    val is_favorite: Boolean,
    val name: String,
    val phone_number: String,
    val poster: String,
    val rating: String,
    val review_count: Int,
    val services: List<Service>,
    val telegram: String,
    val type: String,
    val website: String,
    val work_time: String
)