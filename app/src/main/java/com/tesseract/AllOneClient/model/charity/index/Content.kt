package com.tesseract.AllOneClient.model.charity.index

data class Content(
    val donated_from_trips_all: String,
    val donated_from_trips_today: String,
    val donated_overall: String,
    val level: Int,
    val trips: Int
)