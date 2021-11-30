package com.tesseract.AllOneClient.model.order.cityOrderHistory

data class Content(
    val amount: String,
    val bonus_amount: String,
    val distance: String,
    val driver_id: Int,
    val driver_last_location: String,
    val driver_name: String,
    val has_conditioner: Boolean,
    val id: Int,
    val order_type: String,
    val payment_type: String,
    val point_names: List<String>,
    val points: List<String>,
    val tariff: String,
    val time: String,
    val title: String
)