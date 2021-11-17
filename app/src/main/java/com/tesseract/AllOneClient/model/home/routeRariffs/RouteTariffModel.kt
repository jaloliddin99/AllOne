package com.tesseract.AllOneClient.model.home.routeRariffs

import com.google.gson.annotations.SerializedName

data class RouteTariffModel(

    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("content")
    var content: List<RouteTariffContentListModel>


)