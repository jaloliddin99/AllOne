package com.tesseract.AllOneClient.model.home.payments

data class ShareParcelModel(
    val startId: Int,
    val endId: Int,
    val depDate: String,
    val location: String,
    val locationName: String,
    val receiverName: String,
    val receiverPhoneNumber: String,
    val baggage: String,
    val baggagePlaces: String,
    val paymentType:String,
    val usedBonus:Boolean,
    val usedBonusAmount:Double,
    val orderAmount:Double,
    val baggagePhoto: ArrayList<String>,
    val hasOverheadLuggage: Boolean,
    val forAnother: Boolean,
    val phoneNumber: String,
    val comment:String

)
