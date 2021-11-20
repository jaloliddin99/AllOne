package com.tesseract.AllOneClient.model.tourism.main.index

data class Content(
    val uzb_country_id:Int,
    val banners: List<Banner>,
    val explore_countries: List<ExploreCountry>
)