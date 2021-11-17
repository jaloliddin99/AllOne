package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

import com.google.gson.annotations.SerializedName

data class Feature(

    @field:SerializedName("bbox")
    val bbox: List<Double>,
    @field:SerializedName("type")
    val type: String,
    @field:SerializedName("properties")
    val properties: Properties,
    @field:SerializedName("geometry")
    val geometry: Geometry,
)