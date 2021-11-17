package com.tesseract.AllOneClient.model.home.RouteTariffPrices

import com.google.gson.annotations.SerializedName

class PlacesModel(

    @field:SerializedName("places")
    var places: List<RouteTariffPlaceListModel>?=null,

    @field:SerializedName("parcels")
    var parcels: List<RouteTariffParcelListModel>?=null
)