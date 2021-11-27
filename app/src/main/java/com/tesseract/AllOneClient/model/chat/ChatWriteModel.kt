package com.tesseract.AllOneClient.model.chat

data class ChatWriteModel(
    val client_id: Int,
    val content: String,
    val direction: String,
    val driver_id: Int,
    val order_id: Int,
    val type: String
)