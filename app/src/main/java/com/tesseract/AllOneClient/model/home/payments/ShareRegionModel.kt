package com.tesseract.AllOneClient.model.home.payments

data class ShareRegionModel(
    val startId: Int,
    val endId: Int,
    val tariff: String,
    val userNumber: Int,
    val selectedPlaces: String,
    val depDate: String,
    val location: String,
    val seat: String,
    val parcelPlaces: String,
    val hasOverheadLuggage: Boolean,
    val hasConditioner: Boolean,
    val forAnother: Boolean,
    val phoneNumber: String,
    val money: Double,
    val selectedSeatMoney:Double,
    val used_bonus:Boolean,
    val used_bonus_amount: Double,
    val paymentType: String,
    val comment:String
)
