package com.tesseract.AllOneClient.model.order.activeOrderCity

data class Content(
    val amount: String,
    val bonus_amount: String,
    val comments: String,
    val driver_car: String,
    val driver_id: Int,
    val driver_last_location: String,
    val driver_name: String,
    val driver_phone_number: String,
    val driver_rating: Double,
    val driver_telegram: String,
    val eta: Int,
    val for_another_phone_number: String,
    val has_conditioner: Boolean,
    val id: Int,
    val order_status: String,
    val order_type: String,
    val payment_type: String,
    val point_names: List<String>,
    val points: List<String>,
    val share_order: String,
    val tariff: String,
    val title: String
)