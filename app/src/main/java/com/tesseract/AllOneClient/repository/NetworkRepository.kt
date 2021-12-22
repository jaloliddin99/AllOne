package com.tesseract.AllOneClient.repository

import com.tesseract.AllOneClient.API.APIInterface
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPost
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelBody
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateAddressBody
import javax.inject.Inject

class NetworkRepository @Inject
constructor(private val apiInterface: APIInterface) {

    suspend fun saveDetails(phone: String) = apiInterface.saveDetails(phone)
    suspend fun loginUser(phone: String, code: String) = apiInterface.loginUser(phone, code)
    suspend fun register(token: String, name: String, gender: String, birthdate: String) =
        apiInterface.register(token, name, gender, birthdate)

    suspend fun getRegions(token: Map<String, String>) = apiInterface.getRegions(token)
    suspend fun getDistricts(token: Map<String, String>, regionId: String) =
        apiInterface.getDistricts(token, regionId)

    suspend fun getRouteTariffs(token: Map<String, String>, startPoint: String, endPoint: String) =
        apiInterface.getRouteTariffs(token, startPoint, endPoint)

    suspend fun getRouteTariffPrices(
        token: Map<String, String>,
        orderType: String,
        startPoint: String,
        endPoint: String
    ) =
        apiInterface.getRouteTariffPrices(token, orderType, startPoint, endPoint)

    suspend fun getActiveOrders(token: Map<String, String>, page: Int) =
        apiInterface.getActiveOrders(token, page)

    suspend fun getInterAreaOrderHistory(
        token: Map<String, String>,
        from: String,
        to: String,
        page: Int
    ) = apiInterface.getInterAreaOrderHistory(token, from, to, page)


    suspend fun newOrderInter(
        token: Map<String, String>,
        startPoint: Int,
        endPoint: Int,
        tariff: String,
        passangerCount: Int,
        places: String,
        depDate: String,
        location: String,
        locationName:String,
        baggage: String,
        baggagePlaces: String,
        hasLuggage: Int,
        hasConditioner: Int,
        forAnother: Int,
        phoneNumber: String,
        paymentType: String,
        usedBosus: Int,
        usedAmount: Double,
        orderAmount: Double,
        baggageAmount:Double,
        comment: String,
        cardId: Int

    ) =
        apiInterface.newOrderInterArea(
            token, startPoint, endPoint, tariff, passangerCount,
            places, depDate, location, locationName, baggage, baggagePlaces, hasLuggage,
            hasConditioner, forAnother, phoneNumber, paymentType, usedBosus, usedAmount,
            orderAmount, baggageAmount, comment, cardId
        )


    suspend fun newOrderUpdate(
        token: Map<String, String>,
        id: Int,
        passengerCount: Int,
        places: String
    ) =
        apiInterface.updateNewOrder(token, id, passengerCount, places)

    suspend fun interAreaBooking(
        token: Map<String, String>,
        orderId:Int,
        driverId: Int
    )=apiInterface.interAreaBooking(token, orderId, driverId)

    suspend fun updateData(
        token: Map<String, String>,
        name: String,
        gender: String,
        birthdate: String
    ) = apiInterface.updateData(token, name, gender, birthdate)

    suspend fun sendCode(token: Map<String, String>, phone: String) =
        apiInterface.sendCode(token, phone)

    suspend fun changeLang(token: Map<String, String>, lang:String) =
        apiInterface.changeLang(token, lang)

    suspend fun updatePhone(token: Map<String, String>, phone: String, code: String) =
        apiInterface.updatePhone(token, phone, code)

    suspend fun updateAvatar(token: Map<String, String>, avatar:String)=
        apiInterface.updateAvatar(token, avatar)

    suspend fun getAllBonuses(token: Map<String, String>, page:Int)=
        apiInterface.getAllBonuses(token, page)

    suspend fun getContacts(token: Map<String, String>)=
        apiInterface.getContacts(token)

    suspend fun getParcelRouteTariffPrices(
        token: Map<String, String>,
        orderType: String,
        startPoint: String,
        endPoint: String
    ) =
        apiInterface.getParcelRouteTariffPrices(token, orderType, startPoint, endPoint)


    //parcel new order related apis
    suspend fun parcelNewOrder(
        token: Map<String, String>,
        startPoint: Int,
        endPoint: Int,
        depDate: String,
        depTime: String,
        location: String,
        location_name: String,
        receiverName: String,
        receiverPhone: String,
        baggage: String,
        baggagePlaces: String,
        paymentType: String,
        usedBonus: Int,
        usedBonusAmount: Double,
        orderAmount: Double,
        baggagePhotos:ArrayList<String>,
        hasOverheadLuggage: Int,
        forAnother: Int,
        phoneNumber: String,
        comment: String,
        cardId: Int
    ) =
        apiInterface.newOrderParcel(
            token,
            startPoint,
            endPoint,
            depDate,
            depTime,
            location,
            location_name,
            receiverName,
            receiverPhone,
            baggage,
            baggagePlaces,
            paymentType,
            usedBonus,
            usedBonusAmount,
            orderAmount,
            baggagePhotos,
            hasOverheadLuggage,
            forAnother,
            phoneNumber,
            comment,
            cardId
        )

    //parcel search
    suspend fun parcelSearch(token: Map<String, String>, id: Int) =
        apiInterface.parcelSearch(token, id)

    suspend fun parcelUpdate(token: Map<String, String>, id: Int, baggage_places: String) =
        apiInterface.parcelUpdatePlaces(token, id, baggage_places)

    suspend fun parcelBooking(token: Map<String, String>, orderId: Int, driverId: Int)=
        apiInterface.parcelBooking(token, orderId,driverId)


    suspend fun taxiGetActiveOrderRepo(token: Map<String, String>, id: Int) =
        apiInterface.taxiGetActiveOrder(token, id)


    suspend fun getParcelActiveOrders(token: Map<String, String>, id: Int) =
        apiInterface.getParcelActiveOrders(token, id)

    suspend fun getCityActiveOrders(token: Map<String, String>, orderId: Int)=
        apiInterface.getCityActiveOrders(token, orderId)

    suspend fun getTaxiOrderHistory(token: Map<String, String>, id: Int) =
        apiInterface.getTaxiOrderHistory(token, id)

    suspend fun getParcelDeliveryOrder(token: Map<String, String>, id: Int) =
        apiInterface.getParcelDeliveryOrder(token, id)

    suspend fun getOrderCityHistory(token: Map<String, String>, id: Int)=
        apiInterface.getOrderCityHistory(token, id)

    suspend fun getAboutDriver(token: Map<String, String>, id: Int) =
        apiInterface.getAboutDriver(token, id)

    suspend fun getLocationReverse(token: Map<String, String>, latLng: String) =
        apiInterface.getLocationReverse(token, latLng)

    suspend fun postStoreCard(
        token: Map<String, String>,
        name: String,
        num: String,
        validity: String
    ) =
        apiInterface.storeNewCard(token, name, num, validity)

    suspend fun postActivateCard(token: Map<String, String>, id: Int, code: String) =
        apiInterface.activateCard(token, id, code)

    suspend fun getCardData(token: Map<String, String>) = apiInterface.getCards(token)

    suspend fun updateCard(token: Map<String, String>, id: Int, cardName: String) =
        apiInterface.updateCard(token, id, cardName)

    suspend fun deleteCard(token: Map<String, String>, id: Int) = apiInterface.deleteCard(token, id)

    suspend fun getSavedAddresses(token: Map<String, String>) =
        apiInterface.getSavedAddresses(token)

    suspend fun updateSavedAddress(
        token: Map<String, String>,
        id: Int,
        updateAddressBody: UpdateAddressBody
    ) =
        apiInterface.updateSavedAddress(token, id, updateAddressBody)

    suspend fun deleteSavedAddress(token: Map<String, String>, id: Int) =
        apiInterface.deleteSavedAddress(token, id)

    suspend fun postNewAddress(token: Map<String, String>, fields: Map<String, String>) =
        apiInterface.postNewAddress(token, fields)

    suspend fun getLocationSearch(token: Map<String, String>, location: String) =
        apiInterface.getLocationSearch(token, location)

    suspend fun getNewsMain(token: Map<String, String>) = apiInterface.getNewsMain(token)

    suspend fun getAllNews(token: Map<String, String>, id: Int) = apiInterface.getAllNews(token, id)

    suspend fun searchOrderRegion(token: Map<String, String>, id: Int) =
        apiInterface.searchRegionOrder(token, id)

    //charity
    suspend fun charityIndex(token: Map<String, String>) =
        apiInterface.charityIndex(token)

    suspend fun charityHistory(
        token: Map<String, String>,
        type: String,
        from: String,
        to: String,
        page: Int
    ) =
        apiInterface.charityHistory(token, type, from, to, page)

    suspend fun charityProjects(token: Map<String, String>, page: Int) =
        apiInterface.charityProjects(token, page)

    suspend fun charityCreditCards(token: Map<String, String>, id: Int) =
        apiInterface.charityCreditCards(token, id)

    suspend fun charityDOnate(
        token: Map<String, String>,
        id: Int,
        client_card_id: Int,
        charity_project_card_id: Int,
        amount: Double
    ) =
        apiInterface.charityDOnate(token, id, client_card_id, charity_project_card_id, amount)

    suspend fun cancelOrder(token: Map<String, String>, type: String) =
        apiInterface.cancelOrder(token, type)

    suspend fun cancelOrderPost(
        token: Map<String, String>,
        type: String,
        id: Int,
        cancelBody: CancelBody
    ) =
        apiInterface.cancelOrderPost(token, type, id, cancelBody)

    //make complaint to driver

    suspend fun complaintToDriver(token: Map<String, String>, type: String) =
        apiInterface.getDriverComplaintOptions(token, type)

    suspend fun makeComplaintToDriver(
        token: Map<String, String>,
        id: Int,
        driverId: Int,
        reason: String,
        comment: String
    ) = apiInterface.makeComplaintToDriver(token, id, driverId, reason, comment)

    //driver rating

    suspend fun driverRatingOptions(token: Map<String, String>, orderType: String) =
        apiInterface.getDriverRatingOptions(token, orderType)

    suspend fun driverRatingPost(
        token: Map<String, String>,
        driverId: Int,
        body: DriverRatingPost
    ) = apiInterface.driverRatingPost(token, driverId, body)


    //CITY ==============================

    suspend fun cityRouteTariffs(token: Map<String, String>, points: ArrayList<String>) =
        apiInterface.cityTariffItems(token, points)

    suspend fun cityNewOrder(
        token: Map<String, String>,
        points:ArrayList<String>,
        tariff: String,
        has_overhead_luggage: Int,
        has_conditioner: Int,
        for_another: Int,
        phone_number: String,
        receiver_phone_number: String,
        receiver_comment: String,
        used_bonus: Int,
        used_bonus_amount: Double,
        order_amount: Double,
        payment_type: String,
        comment: String,
        card_id: Int,
        cargo_type: String
    ) = apiInterface.cityNewOrder(
        token,
        points,
        tariff,
        has_overhead_luggage,
        has_conditioner,
        for_another,
        phone_number,
        receiver_phone_number,
        receiver_comment,
        used_bonus,
        used_bonus_amount,
        order_amount,
        payment_type,
        comment,
        card_id,
        cargo_type
    )

    //clinics
    suspend fun clinicsList(
        token: Map<String, String>,
        name: String,
        category_id: Int,
        city_id: Int,
        page: Int
    ) = apiInterface.getClinics(token, name, category_id, city_id, page)

    suspend fun getClinicsCategories(
        token: Map<String, String>
    ) = apiInterface.getClinicsCategories(token)

    suspend fun getClinicView(
        token: Map<String, String>, id: Int
    ) = apiInterface.getClinicView(token, id)

    suspend fun clinicAddToFavourite(
        token: Map<String, String>, name: String, id: Int
    ) = apiInterface.clinicAddToFavourite(token, name, id)

    suspend fun medTourismClinicRate(
        token: Map<String, String>,
        name: String,
        id: Int,
        rating: Int,
        comment: String
    ) =
        apiInterface.medTourismClinicRate(token, name, id, rating, comment)

    suspend fun ambulance(token: Map<String, String>) = apiInterface.ambulance(token)

    suspend fun getDoctors(
        token: Map<String, String>,
        name: String,
        category_id: Int,
        city_id: Int,
        page: Int
    ) = apiInterface.getDoctors(token, name, category_id, city_id, page)


    suspend fun getDoctorsCategories(
        token: Map<String, String>
    )=apiInterface.getDoctorsCategories(token)

    suspend fun getDoctorView(
        token: Map<String, String>,
        id: Int
    )=apiInterface.getDoctorView(token, id)

    suspend fun getMedTurIndex(
        token: Map<String, String>
    )=apiInterface.getMedTurIndex(token)

    suspend fun getFavourites(token: Map<String, String>, medOrTour:String, page: Int)=apiInterface.getFavourites(token, medOrTour, page)

    suspend fun deleteFromFavourites(token: Map<String, String>, medOrTour:String, pageId:Int, type: String)=
        apiInterface.deleteFromFavourites(token, medOrTour, pageId, type)

//    suspend fun doctorAddToFavourite(
//        token: Map<String, String>,
//        id: Int
//    ) = apiInterface.doctorAddToFavourite(token, id)
//
//    suspend fun medTourismDoctorRate(
//        token: Map<String, String>,
//        id: Int,
//        rating: Int,
//        comment: String
//    ) =
//        apiInterface.medTourismDoctorRate(token, id, rating, comment)








    ///=======================tourism

    suspend fun tourismMainIndex(token: Map<String, String>)=apiInterface.getTourismIndex(token)

    suspend fun getTourIndex(token: Map<String, String>, location: String, query:String, countryId:Int, currencyId:Int, sort:String, page: Int)=
        apiInterface.getIndexUzb(token, location, query, countryId, currencyId, sort, page)

    suspend fun packageView(token: Map<String, String>, packageId:Int)=apiInterface.tourPackageView(token, packageId)

    suspend fun packageAddToFav(token: Map<String, String>, id: Int)=apiInterface.addToFavTour(token, id)

    suspend fun getTourExplore(token: Map<String, String>)=apiInterface.getTourExplore(token)

    suspend fun exploreCountryPackages(token: Map<String, String>, id: Int, page: Int)=apiInterface.exploreCountryPackages(token, id, page)

    suspend fun exploreCountryView(token: Map<String, String>, id: Int)=apiInterface.exploreCountryView(token, id)

    suspend fun getAllCountries(token: Map<String, String>)=apiInterface.getAllCountries(token)

    suspend fun getAllCurrencies(token: Map<String, String>)=apiInterface.getCurrencies(token)

    //car rent

    suspend fun getCarRentIndexMain(token: Map<String, String>)=apiInterface.getCarRentIndexMain(token)

    suspend fun getCarRentCar(token:Map<String, String>,
                              query:String, countryId:Int,
                              currencyId:Int, sort:String,carId:Int, page:Int)
    =apiInterface.getCarRentCars(token, query, countryId, currencyId, sort, carId, page)

    suspend fun getCarCompanies(token: Map<String, String>)=apiInterface.getCarCompanies(token)

    suspend fun getCarModels(token: Map<String, String>)=apiInterface.getCarModels(token)

    suspend fun getCarMarkas(token: Map<String, String>)=apiInterface.getCarMarkas(token)

    suspend fun getCarView(token: Map<String, String>, carId:Int)=apiInterface.getCarView(token, carId)

    suspend fun carRentAddToFavourites(token: Map<String, String>, packageId:Int)=apiInterface.carRentAddToFavourites(token, packageId)


    //HOTELS

    suspend fun getHotelIndex(token: Map<String, String>, query: String, countryId: Int, currencyId: Int, sort: String, page: Int)=
        apiInterface.getHotelIndex(token, query, countryId, currencyId, sort, page)

    suspend fun getHotelView(token: Map<String, String>, hotelId:Int)=
        apiInterface.getHotelView(token, hotelId)

    suspend fun hotelAddToFav(token: Map<String, String>, hotelId:Int)=
        apiInterface.hotelAddToFav(token, hotelId)

    suspend fun getRoomView(token: Map<String, String>, hotelId: Int, roomId:Int)=
        apiInterface.getRoomView(token, hotelId, roomId)
    /*=============AGENCY ===================*/

    suspend fun getTravelAgencies(token: Map<String, String>, query: String, countryId: Int, sort: String, page: Int)=
        apiInterface.getTravelAgencies(token, query, countryId, sort, page)

    suspend fun getAgencyView(token: Map<String, String>, id: Int)=
        apiInterface.getAgencyView(token, id)

    suspend fun getAgencyPackageView(token: Map<String, String>, id: Int, page:Int,)=
        apiInterface.getAgencyPackageView(token, id, page)

    suspend fun postAddToFavourites(token: Map<String, String>, id: Int)=
        apiInterface.agencyAddToFavourite(token, id)

    /*******************************CHAT API*******************************************/
    /**Chat api*/
    suspend fun chatModel(token: Map<String, String>, orderID: Int)=apiInterface.getChatModel(token, orderID)




}