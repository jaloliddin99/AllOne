package com.tesseract.AllOneClient.model.order

data class MessageEvent(
    val position: Int,
    val tariff: String?,
    val id: Int
)
