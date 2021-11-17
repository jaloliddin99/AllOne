package com.tesseract.AllOneClient.model.medTourism.clinicServices

data class Service(
    val description: String,
    val name: String,
    val price: String,
    val price_list: List<Price>
)