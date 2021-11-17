package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

data class Query(
    val coordinates: List<List<Double>>,
    val format: String,
    val profile: String
)