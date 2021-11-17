package com.tesseract.AllOneClient.model.medTourism.categories

import java.io.Serializable

data class ClinicsCategoriesModel(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
):Serializable