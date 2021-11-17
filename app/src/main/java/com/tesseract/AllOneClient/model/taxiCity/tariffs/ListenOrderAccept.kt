package com.tesseract.AllOneClient.model.taxiCity.tariffs

data class ListenOrderAccept(
    val bearing: Int,
    val driver_id: Int,
    val location: String,
    val ride: Int,
    val ride_amount: String,
    val ride_time: Int,
    val status: String,
    val wait_time: Int,
    val wait_time_amount: String
)