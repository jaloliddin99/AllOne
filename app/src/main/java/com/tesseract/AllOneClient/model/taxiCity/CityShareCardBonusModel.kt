package com.tesseract.AllOneClient.model.taxiCity

import com.tesseract.AllOneClient.model.profile.getCards.GetCardData
import java.io.Serializable

data class CityShareCardBonusModel(
    val cardType:String,
    val cardNumber:String,
    val cardId:Int,
    val bonusAmount:String,
    val paymentType:String
):Serializable
