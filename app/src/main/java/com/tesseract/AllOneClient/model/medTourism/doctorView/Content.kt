package com.tesseract.AllOneClient.model.medTourism.doctorView

data class Content(
    val addr: String,
    val clinics: List<Clinic>,
    val description: String,
    val id: Int,
    val is_favorite: Boolean,
    val location: String,
    val name: String,
    val phone_number: List<String>,
    val poster: String,
    val rating: String,
    val review_count: Int,
    val telegram: String,
    val type: String,
    val work_time: String
)