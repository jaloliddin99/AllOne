package com.tesseract.AllOneClient.model.taxiCity

import java.io.Serializable

data class Contact(val id: String, val name:String) :Serializable{
    var numbers = ArrayList<String>()
    var emails = ArrayList<String>()
    var isCancelled:Boolean = true
}