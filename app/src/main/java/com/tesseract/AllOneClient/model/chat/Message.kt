package com.tesseract.AllOneClient.model.chat

data class Message(
    val chat_id: Int,
    val content: String,
    val created_at: String,
    val direction: String,
    val id: Int,
    val type: String
)