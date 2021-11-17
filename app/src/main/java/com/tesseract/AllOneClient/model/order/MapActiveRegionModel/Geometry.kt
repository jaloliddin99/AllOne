package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

import com.google.gson.annotations.SerializedName

data class Geometry(
    @field:SerializedName("coordinates")
    val coordinates: List<List<Double>>,

    @field:SerializedName("type")
    val type: String

)