package com.tesseract.AllOneClient.API

import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.MapActiveRegionModel
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface APIRouting {

    @POST("directions/driving-car/geojson")
    suspend fun locationRouting(
        @HeaderMap map:Map<String, String>,
        @Body sendRoutingDetails: RoutindDetails,
    ): Response<MapActiveRegionModel>
}