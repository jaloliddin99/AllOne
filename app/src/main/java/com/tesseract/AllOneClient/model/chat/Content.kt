package com.tesseract.AllOneClient.model.chat

data class Content(
    val driver_id: Int,
    val driver_avatar: String,
    val driver_name: String,
    val messages: List<Message>,
    val order_id: Int
)