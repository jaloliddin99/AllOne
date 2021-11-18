package com.tesseract.AllOneClient.model.medTourism.medMain

data class Content(
    val nearby_clinics: List<NearbyClinic>,
    val popular_categories: List<PopularCategory>,
    val popular_clinics: List<PopularClinic>,
    val popular_doctors: List<PopularDoctor>
)