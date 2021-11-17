package com.tesseract.AllOneClient.model.dialogComplaint

data class DialogComplaintModel(
    val content: List<Content>,
    val message: Any,
    val success: Boolean
)