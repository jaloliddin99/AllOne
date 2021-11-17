package com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel

import com.google.gson.annotations.SerializedName

data class OrderHistoryContent(
    @field:SerializedName("current_page")
    var currentPage: String?=null,

    @field:SerializedName("data")
    var  orderHistoryDataData: List<OrderHistoryDataListModel>?=null
)
