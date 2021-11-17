package com.tesseract.AllOneClient.model.taxiCity.updateSaved

import com.tesseract.AllOneClient.model.taxiCity.StationModel
import java.io.Serializable

data class CityShareStationModel(
    var stationModel:List<StationModel>
):Serializable
