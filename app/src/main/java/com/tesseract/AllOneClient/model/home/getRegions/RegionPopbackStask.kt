package com.tesseract.AllOneClient.model.home.getRegions

import java.io.Serializable

data class RegionPopbackStask(
    val locationName:String,
    val locationId:Int,
    val direction:Int
):Serializable
