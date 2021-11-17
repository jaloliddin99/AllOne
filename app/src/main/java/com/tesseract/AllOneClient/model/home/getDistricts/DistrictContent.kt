package com.tesseract.AllOneClient.model.home.getDistricts

import com.google.gson.annotations.SerializedName

data class DistrictContent(
    @field:SerializedName("region")
    var region: RegionName?=null,

    @field:SerializedName("districts")
    var districtList: List<DistrictList>?=null

)
