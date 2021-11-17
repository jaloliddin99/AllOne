package com.tesseract.AllOneClient.model.home.news

import com.google.gson.annotations.SerializedName

class NewsItemModel(
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("image")
    var image: String? = null,

    @field:SerializedName("title")
    var title: String? = null,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("date")
    var date: String? = null
)