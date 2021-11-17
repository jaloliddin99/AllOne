package com.tesseract.AllOneClient.model.home.routeRariffs

import com.google.gson.annotations.SerializedName

data class RouteTariffContentListModel(
    @field:SerializedName("id")
    var id : Int?=null,

    @field:SerializedName("type")
    var type: String?=null,

    @field:SerializedName("name")
    var name: String?=null,

    @field:SerializedName("desc")
    var desc: String?=null,

    @field:SerializedName("price")
    var price: String?=null,

    @field:SerializedName("discount")
    var discount : String?=null,

    @field:SerializedName("icon")
    var icon : String?=null
)
