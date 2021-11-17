package com.tesseract.AllOneClient.model.dialogComplaint

import com.google.gson.annotations.SerializedName

data class MakeComplaintDriverResponseModel(
    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("success")
    val success: Boolean
)