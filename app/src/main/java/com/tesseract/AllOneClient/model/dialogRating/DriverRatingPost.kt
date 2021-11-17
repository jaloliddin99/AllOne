package com.tesseract.AllOneClient.model.dialogRating

data class DriverRatingPost(
    val order_id: Int,
    val rating: Int,
    val rating_options: String,
    val review: String
)