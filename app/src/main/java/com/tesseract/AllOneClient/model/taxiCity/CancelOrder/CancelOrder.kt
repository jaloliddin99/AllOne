package com.tesseract.AllOneClient.model.taxiCity.CancelOrder

data class CancelOrder(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
)