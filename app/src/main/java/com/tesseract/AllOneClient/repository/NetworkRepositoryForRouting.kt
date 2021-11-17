package com.tesseract.AllOneClient.repository

import com.tesseract.AllOneClient.API.APIInterface
import com.tesseract.AllOneClient.API.APIRouting
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import javax.inject.Inject

class NetworkRepositoryForRouting @Inject
constructor(private val apiInterface: APIRouting) {

    suspend fun routing(token:Map<String, String>, body: RoutindDetails)=apiInterface.locationRouting(token, body)



}