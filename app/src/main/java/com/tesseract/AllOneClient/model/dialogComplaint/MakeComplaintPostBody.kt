package com.tesseract.AllOneClient.model.dialogComplaint

data class MakeComplaintPostBody(
    val comment: String,
    val driver_id: Int,
    val reason: String
)