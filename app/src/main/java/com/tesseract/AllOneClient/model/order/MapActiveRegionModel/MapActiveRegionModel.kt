package com.tesseract.AllOneClient.model.order.MapActiveRegionModel

import com.google.gson.annotations.SerializedName

data class MapActiveRegionModel(

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("metadata")
    val metadata: Metadata,

    @field:SerializedName("features")
    val features: List<Feature>,

    @field:SerializedName("bbox")
    val bbox: List<Double>,

)