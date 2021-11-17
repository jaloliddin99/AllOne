package com.tesseract.AllOneClient.model.charity.projectCards

data class Content(
    val cards: List<Card>,
    val id: Int,
    val image: String,
    val title: String
)