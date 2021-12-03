package com.tesseract.AllOneClient.model.charity.history

data class Data(
    val date: String,
    val date_by_words: String,
    val charities: List<Charities>
)