package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

import com.google.gson.annotations.SerializedName

data class RoutindDetails(
    @field:SerializedName("instructions")
    var instructions: Boolean,

    @field:SerializedName("coordinates")
    var coordinates: ArrayList<ArrayList<Double>>
)
