package com.tesseract.AllOneClient.model.home.SearchModel

data class Content(
    val found: String,
    val order: Order,
    val other_options: List<OtherOption>,
    val your_request: List<YourRequest>
)