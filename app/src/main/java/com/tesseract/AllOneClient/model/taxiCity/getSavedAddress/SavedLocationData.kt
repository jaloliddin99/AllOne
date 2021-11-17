package com.tesseract.AllOneClient.model.taxiCity.getSavedAddress

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class SavedLocationData(
    @field:SerializedName("id")
    var id: Int? = null,
    @field:SerializedName("client_id")
    var clientId: Int? = null,
    @field:SerializedName("type")
    var type: String? = null,
    @field:SerializedName("name")
    var name: String? = null,
    @field:SerializedName("address")
    var address: String? = null,

    @field:SerializedName("latlng")
    var latLng: String? = null,
    @field:SerializedName("created_at")
    var createdAt: String? = null,

    ):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<SavedLocationData> {
        override fun createFromParcel(parcel: Parcel): SavedLocationData {
            return SavedLocationData(parcel)
        }

        override fun newArray(size: Int): Array<SavedLocationData?> {
            return arrayOfNulls(size)
        }
    }

}
