package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

data class Metadata(
    val attribution: String,
    val engine: Engine,
    val query: Query,
    val service: String,
    val timestamp: Long
)