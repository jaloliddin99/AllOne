package com.tesseract.AllOneClient.model.login

import com.google.gson.annotations.SerializedName

data class UsedDetailsModel(

    @field:SerializedName("id")
    var id:Int?=null,

    @field:SerializedName("phone")
    var phone:String?=null,

    @field:SerializedName("name")
    var name: String?=null,

    @field:SerializedName("gender")
    var gender: String?=null,

    @field:SerializedName("birthdate")
    var birthdate: String?=null,

    @field:SerializedName("balance")
    var balance: String?=null,

    @field:SerializedName("created_at")
    var created_at: String?=null

)