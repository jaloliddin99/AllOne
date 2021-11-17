package com.tesseract.AllOneClient.model.login

import com.google.gson.annotations.SerializedName


data class ModelClass(

    @field:SerializedName("success")
    var success: Boolean?=null,

    @field:SerializedName("content")
    var content: String?=null,

    )