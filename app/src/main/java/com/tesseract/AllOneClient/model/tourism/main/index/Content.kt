package com.tesseract.AllOneClient.model.tourism.main.index

data class Content(
    val uzb_country_id:Int,
    val uzb_default_currency_id:Int,
    val world_default_currency_id:Int,
    val default_sort:String,
    val banners: List<Banner>,
    val explore_countries: List<ExploreCountry>
)