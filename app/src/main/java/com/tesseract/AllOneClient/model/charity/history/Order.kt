package com.tesseract.AllOneClient.model.charity.history

data class Order(
    val amount: String,
    val id: Int,
    val time: String,
    val title: String,
    val type: String
)