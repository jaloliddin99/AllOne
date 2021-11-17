package com.tesseract.AllOneClient.model.taxiCity.updateSaved

data class UpdateAddressBody(
    val address: String,
    val latlng: String,
    val name: String
)