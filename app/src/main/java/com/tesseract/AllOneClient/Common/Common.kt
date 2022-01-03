package com.tesseract.AllOneClient.Common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.model.tourism.ImageModel
import java.util.*

object Common {

    var fromWhichLayout=0
    var getAllPonusesPage=1
    var agencyPackagesPager=1
    var travelPagerId=1
    var hotelIndexPager=1
    var carRentPageId=1
    var countryPageee=1
    var donationCountPage = 1
    var donationProjects = 1

    var isCurrentRegionFragment:Boolean=true
    var countPage = 1
    var countPageMain = 1
    var orderHistoryCountPage = 1
    var allNewsCounter = 1

    var isCityTariffPreviousBackstack=false

    var cityTariffRecyclerView=0

    var clinicsPaging=1

    var doctorPaging=1

    var favouritesPagingApi=1

    var tourIndexMain=1
    var questionNumbers: ArrayList<ImageModel> = ArrayList<ImageModel>()

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun transparency(percentage: Int): String {
        var catcher = ""
        when (percentage) {
            0 -> {
                catcher = "00"
            }
            10 -> {
                catcher = "1A"
            }
            20 -> {
                catcher = "33"
            }
            30 -> {
                catcher = "4D"
            }
            40 -> {
                catcher = "66"
            }
            50 -> {
                catcher = "80"
            }
            60 -> {
                catcher = "99"
            }
            70 -> {
                catcher = "B3"
            }
            80 -> {
                catcher = "CC"
            }
            90 -> {
                catcher = "E6"
            }
            100 -> {
                catcher = "FF"
            }
        }
        return catcher
    }


   var startRegion: String=""
   var startDistrict: String=""
   var endRegion: String=""
   var endDistrict: String=""
   var destination:Int=-1
   var startRegionId: String=""
   var startDistrictId: String=""
   var endRegionId: String=""
   var endDistrictId: String=""

}