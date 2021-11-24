package com.tesseract.AllOneClient.model.tourism.countries

import java.io.Serializable

data class TourismCountries(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
):Serializable