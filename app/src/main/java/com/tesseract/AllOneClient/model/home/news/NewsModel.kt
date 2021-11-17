package com.tesseract.AllOneClient.model.home.news

import com.google.gson.annotations.SerializedName
import com.tesseract.AllOneClient.model.home.LocationSearch.LocationSearchList

data class NewsModel(
    @field:SerializedName("success")
    var success: Boolean? = null,

    @field:SerializedName("message")
    var message: String? = null,

    @field:SerializedName("content")
    var getDistrictContent: NewsItemModel? = null
)