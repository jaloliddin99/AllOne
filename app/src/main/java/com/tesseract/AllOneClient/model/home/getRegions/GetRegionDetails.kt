package com.tesseract.AllOneClient.model.home.getRegions

import com.google.gson.annotations.SerializedName

data class GetRegionDetails(

    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("direct")
    var direct: Boolean? = null
)