package com.tesseract.AllOneClient.model.medTourism.medMain

data class Content(
    val uzb_country_id:String,
    val uzb_default_currency_id:String,
    val world_default_currency_id:String,
    val default_sort:String,
    val nearby_clinics: List<NearbyClinic>,
    val popular_categories: List<PopularCategory>,
    val popular_clinics: List<PopularClinic>,
    val popular_doctors: List<PopularDoctor>
)