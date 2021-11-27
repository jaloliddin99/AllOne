package com.tesseract.AllOneClient.model.chat

data class ChatSocketModel(
    val content: Content,
    val message: Any,
    val success: Boolean
)