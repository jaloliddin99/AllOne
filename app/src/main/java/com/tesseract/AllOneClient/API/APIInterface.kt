package com.tesseract.AllOneClient.API

import com.tesseract.AllOneClient.model.tourism.hotels.index.HotelIndex
import com.tesseract.AllOneClient.model.tourism.carRent.carView.CarViewModel
import com.tesseract.AllOneClient.model.charity.donate.CharityDonate
import com.tesseract.AllOneClient.model.charity.history.CharityHistoryMain
import com.tesseract.AllOneClient.model.charity.index.Index
import com.tesseract.AllOneClient.model.charity.projectCards.ProjectCreditCardModel
import com.tesseract.AllOneClient.model.charity.projects.CharityProjectMainModel
import com.tesseract.AllOneClient.model.dialogComplaint.DialogComplaintModel
import com.tesseract.AllOneClient.model.dialogComplaint.MakeComplaintDriverResponseModel
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingOptions
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPost
import com.tesseract.AllOneClient.model.dialogRating.DriverRatingPostResponse
import com.tesseract.AllOneClient.model.home.LocationSearch.LocationSearchModel
import com.tesseract.AllOneClient.model.home.RouteTariffPrices.RouteTariffPricesModel
import com.tesseract.AllOneClient.model.home.SearchModel.SearchRegionOrderModel
import com.tesseract.AllOneClient.model.home.getDistricts.GetDistrictsModel
import com.tesseract.AllOneClient.model.home.getRegions.GetRegions
import com.tesseract.AllOneClient.model.taxiCity.getSavedAddress.GetSavedAddressModel
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryModel
import com.tesseract.AllOneClient.model.home.news.NewsModel
import com.tesseract.AllOneClient.model.home.news.allNews.ALlNewsModel
import com.tesseract.AllOneClient.model.home.nowOrder.NewOrderInterAreaModel
import com.tesseract.AllOneClient.model.home.postNewAddressModel.PostNewAddressModel
import com.tesseract.AllOneClient.model.home.routeRariffs.RouteTariffModel
import com.tesseract.AllOneClient.model.home.updateNewOrder.NewOrderUpdateModel
import com.tesseract.AllOneClient.model.login.LoginModel
import com.tesseract.AllOneClient.model.login.ModelClass
import com.tesseract.AllOneClient.model.login.RegisterModel
import com.tesseract.AllOneClient.model.medTourism.ambulance.AmbulanceModel
import com.tesseract.AllOneClient.model.medTourism.categories.ClinicsCategoriesModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicAddToFavouriteModel
import com.tesseract.AllOneClient.model.medTourism.clinicServices.ClinicMainModel
import com.tesseract.AllOneClient.model.medTourism.clinics.ClinicsMainModel
import com.tesseract.AllOneClient.model.medTourism.doctorView.DoctorViewMainModel
import com.tesseract.AllOneClient.model.medTourism.favourites.FavouritesModel
import com.tesseract.AllOneClient.model.medTourism.medMain.MedTurMainModel
import com.tesseract.AllOneClient.model.order.aboutDriverModel.AboutDriverModel
import com.tesseract.AllOneClient.model.order.getActiveOrderModel.GetActiveOrderModel
import com.tesseract.AllOneClient.model.order.getActiveParcelOrdersModel.GetActiveParcelOrderModel
import com.tesseract.AllOneClient.model.order.getOrderParcelModel.OrderParcelATModel
import com.tesseract.AllOneClient.model.order.getTaxiOrderHistory.OrderRegionATModel
import com.tesseract.AllOneClient.model.parcel.SelectLocationModel
import com.tesseract.AllOneClient.model.parcel.parcelSearch.ParcelSearchFoundModel
import com.tesseract.AllOneClient.model.parcel.parcelUpdate.ParcelUpdateModel
import com.tesseract.AllOneClient.model.profile.SendCode
import com.tesseract.AllOneClient.model.profile.card.ActivateCard
import com.tesseract.AllOneClient.model.profile.card.StoreCard
import com.tesseract.AllOneClient.model.profile.getCards.GetCardModel
import com.tesseract.AllOneClient.model.profile.updateDelete.UpdateDeleteModel
import com.tesseract.AllOneClient.model.taxiCity.CancelOrder.CancelOrder
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelBody
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelOrderPost
import com.tesseract.AllOneClient.model.taxiCity.deleteSavedAddress.DeleteSavedAddressModel
import com.tesseract.AllOneClient.model.taxiCity.newOrder.CityCreateNewOrder
import com.tesseract.AllOneClient.model.taxiCity.tariffs.CityTariffMainModel
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateAddressBody
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateSavedLocationModel
import com.tesseract.AllOneClient.model.tourism.carRent.cars.CarRentCarsModel
import com.tesseract.AllOneClient.model.tourism.carRent.indexMain.CarRentIndexMain
import com.tesseract.AllOneClient.model.tourism.countries.TourismCountries
import com.tesseract.AllOneClient.model.tourism.expCountryPackage.ExploreCountryPackage
import com.tesseract.AllOneClient.model.tourism.expCountryView.ExploreCountryView
import com.tesseract.AllOneClient.model.tourism.explore.TourExploreModel
import com.tesseract.AllOneClient.model.tourism.indexUzb.IndexUzbModel
import com.tesseract.AllOneClient.model.tourism.main.index.TourismMainIndex
import com.tesseract.AllOneClient.model.tourism.packageView.PackageViewMainModel
import retrofit2.Response
import retrofit2.http.*

interface APIInterface {

    @FormUrlEncoded
    @POST("sendcode")
    suspend fun saveDetails(
        @Field("phone") name: String
    ): Response<ModelClass>

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("phone") phone: String,
        @Field("code") code: String
    ): Response<LoginModel>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Header("Authorization") token: String,
        @Field("name") phone: String,
        @Field("gender") gender: String,
        @Field("birthdate") birthdate: String
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("settings/update_data")
    suspend fun updateData(
        @HeaderMap headers: Map<String, String>,
        @Field("name") phone: String,
        @Field("gender") gender: String,
        @Field("birthdate") birthdate: String
    ): Response<RegisterModel>

    @FormUrlEncoded
    @POST("settings/update_phone/sendcode")
    suspend fun sendCode(
        @HeaderMap headers: Map<String, String>,
        @Field("phone") phone: String
    ): Response<SendCode>


    @FormUrlEncoded
    @POST("settings/update_phone")
    suspend fun updatePhone(
        @HeaderMap headers: Map<String, String>,
        @Field("phone") phone: String,
        @Field("code") code: String
    ): Response<RegisterModel>

    //interArea new order

    @FormUrlEncoded
    @POST("interarea/new_order")
    suspend fun newOrderInterArea(
        @HeaderMap headers: Map<String, String>,
        @Field("start_point") startPoint: Int,
        @Field("end_point") endPoint: Int,
        @Field("tariff") tariff: String,
        @Field("passenger_count") passengerCount: Int,
        @Field("places") places: String,
        @Field("departure_date") departure_date: String,
        @Field("location") location: String,
        @Field("baggage") baggage: String,
        @Field("baggage_places") baggage_places: String,
        @Field("has_overhead_luggage") has_overhead_luggage: Boolean,
        @Field("has_conditioner") has_conditioner: Boolean,
        @Field("for_another") for_another: Boolean,
        @Field("phone_number") phone_number: String,
        @Field("payment_type") payment_type: String,
        @Field("used_bonus") used_bonus: Boolean,
        @Field("used_bonus_amount") used_bonus_amount: Double,
        @Field("order_amount") order_amount: Double,
        @Field("comment") comment: String,
        @Field("card_id") cardId:Int
    ) : Response<NewOrderInterAreaModel>


    @POST("interarea/search/{id}")
    suspend fun searchRegionOrder(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ) : Response<SearchRegionOrderModel>

    @FormUrlEncoded
    @POST("interarea/update_place/{id}")
    suspend fun updateNewOrder(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int,
        @Field("passenger_count") passenger_count:Int,
        @Field("places") places: String
    ):Response<NewOrderUpdateModel>



    //parcel delivery related APIS

    @FormUrlEncoded
    @POST("interarea_parcel_delivery/new_order")
    suspend fun newOrderParcel(
        @HeaderMap headers: Map<String, String>,
        @Field("start_point") startPoint: Int,
        @Field("end_point") endPoint: Int,
        @Field("departure_date") departure_date: String,
        @Field("departure_time") departure_time: String,
        @Field("location") location: String,
        @Field("receiver_name") receiverName: String,
        @Field("receiver_phone_number") receiverPhone: String,
        @Field("baggage") baggage: String,
        @Field("baggage_places") baggage_places: String,
        @Field("payment_type") payment_type: String,
        @Field("used_bonus") used_bonus: Boolean,
        @Field("used_bonus_amount") used_bonus_amount: Double,
        @Field("order_amount") order_amount: Double,
        @FieldMap details: Map<String, String>,
        @Field("has_overhead_luggage") has_overhead_luggage: Boolean,
        @Field("for_another") for_another: Boolean,
        @Field("phone_number") phone_number: String,
        @Field("comment") comment: String,
        @Field("card_id") cardId:Int
    ) : Response<NewOrderInterAreaModel>

    @POST("interarea_parcel_delivery/search/{id}")
    suspend fun parcelSearch(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int
    ) : Response<ParcelSearchFoundModel>

    @FormUrlEncoded
    @POST("interarea_parcel_delivery/update_place/{id}")
    suspend fun parcelUpdatePlaces(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id: Int,
        @Field("baggagePlaces") baggagePlaces:String
    ):Response<ParcelUpdateModel>



    /////

    @FormUrlEncoded
    @POST("settings/credit_cards/store")
    suspend fun storeNewCard(
        @HeaderMap headers: Map<String, String>,
        @Field("card_name") cardName:String,
        @Field("card_number") cardNumber:String,
        @Field("card_validity") cardValidity: String
        ) : Response<StoreCard>

    @FormUrlEncoded
    @POST("settings/credit_cards/activate/{id}")
    suspend fun activateCard(
        @HeaderMap headers: Map<String, String>,
        @Path("id") cardId:Int,
        @Field("code") code:String,
    ) : Response<ActivateCard>

    @FormUrlEncoded
    @POST("settings/credit_cards/update/{id}")
    suspend fun updateCard(
        @HeaderMap headers: Map<String, String>,
        @Path("id") cardId:Int,
        @Field("card_name") cardName:String,
    ) : Response<UpdateDeleteModel>


    @POST("settings/credit_cards/delete/{id}")
    suspend fun deleteCard(
        @HeaderMap headers: Map<String, String>,
        @Path("id") cardId:Int
    ) : Response<UpdateDeleteModel>


    //Saved Addresses
    @FormUrlEncoded
    @POST("settings/saved_addresses/store")
    suspend fun postNewAddress(
        @HeaderMap headers: Map<String, String>,
        @FieldMap fields:Map<String, String>
    ):Response<PostNewAddressModel>

    @GET("settings/saved_addresses")
    suspend fun getSavedAddresses(
        @HeaderMap headers: Map<String, String>,
    ): Response<GetSavedAddressModel>

    @POST("settings/saved_addresses/update/{id}")
    suspend fun updateSavedAddress(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int,
        @Body updateAddressBody: UpdateAddressBody
    ):Response<UpdateSavedLocationModel>

    @POST("settings/saved_addresses/delete/{id}")
    suspend fun deleteSavedAddress(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int
    ):Response<DeleteSavedAddressModel>


    @GET("regions")
    suspend fun getRegions(
        @HeaderMap headers: Map<String, String>
    ):Response<GetRegions>

    @GET("regions/{regionId}")
    suspend fun getDistricts(
        @HeaderMap headers: Map<String, String>,
        @Path("regionId") regionId: String
    ):Response<GetDistrictsModel>

    @GET("interarea/route_tariffs")
    suspend fun getRouteTariffs(
        @HeaderMap headers: Map<String, String>,
        @Query("start_point") startPoint: String,
        @Query("end_point") endPoint: String
    ):Response<RouteTariffModel>

    @GET("interarea/route_tariffs/{standart}")
    suspend fun getRouteTariffPrices(
        @HeaderMap headers: Map<String, String>,
        @Path("standart") orderType: String,
        @Query("start_point") startPoint: String,
        @Query("end_point") endPoint: String
    ):Response<RouteTariffPricesModel>


    @GET("interarea_parcel_delivery/route_tariffs/{route_tariffs}")
    suspend fun getParcelRouteTariffPrices(
        @HeaderMap headers: Map<String, String>,
        @Path("route_tariffs") orderType: String,
        @Query("start_point") startPoint: String,
        @Query("end_point") endPoint: String
    ):Response<RouteTariffPricesModel>

    @GET("orders/active")
    suspend fun getActiveOrders(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int
    ):Response<OrderHistoryModel>

    @GET("orders/history")
    suspend fun getInterAreaOrderHistory(
        @HeaderMap headers: Map<String, String>,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("page") page: Int
    ): Response<OrderHistoryModel>

    @GET("orders/active/taxi/{id}")
    suspend fun taxiGetActiveOrder(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<GetActiveOrderModel>


    @GET("orders/active/parcel_delivery/{id}")
    suspend fun getParcelActiveOrders(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<GetActiveParcelOrderModel>


    @GET("orders/history/taxi/{id}")
    suspend fun getTaxiOrderHistory(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ):Response<OrderRegionATModel>

    @GET("orders/history/parcel_delivery/{id}")
    suspend fun getParcelDeliveryOrder(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ):Response<OrderParcelATModel>

    @GET("driver_info/{id}")
    suspend fun getAboutDriver(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ):Response<AboutDriverModel>

    @GET("location/reverse")
    suspend fun getLocationReverse(
        @HeaderMap headers: Map<String, String>,
        @Query("latlng") latLng: String
    ):Response<SelectLocationModel>

    @GET("settings/credit_cards")
    suspend fun getCards(
        @HeaderMap headers: Map<String, String>,
    ):Response<GetCardModel>


    @GET("location/search")
    suspend fun getLocationSearch(
        @HeaderMap headers: Map<String, String>,
        @Query("q") location: String,
    ):Response<LocationSearchModel>

    @GET("news/main")
    suspend fun getNewsMain(
        @HeaderMap headers: Map<String, String>
    ):Response<NewsModel>

    @GET("news/all")
    suspend fun getAllNews(
        @HeaderMap headers: Map<String, String>,
        @Query("page") id: Int
    ):Response<ALlNewsModel>


    //+++++++++++++++charity
    @GET("charity")
    suspend fun charityIndex(
        @HeaderMap headers: Map<String, String>
    ):Response<Index>

    @GET("charity/history")
    suspend fun charityHistory(
        @HeaderMap headers: Map<String, String>,
        @Query("type") fromTrips:String,
        @Query("from") from:String,
        @Query("to") to:String,
        @Query("page") page:Int
    ):Response<CharityHistoryMain>

    @GET("charity/projects")
    suspend fun charityProjects(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page:Int
    ):Response<CharityProjectMainModel>

    @GET("charity/project/{id}/credit_cards")
    suspend fun charityCreditCards(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int
    ):Response<ProjectCreditCardModel>

    @FormUrlEncoded
    @POST("charity/project/{id}/donate")
    suspend fun charityDOnate(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int,
        @Field("client_card_id") client_card_id:Int,
        @Field("charity_project_card_id") charity_project_card_id:Int,
        @Field("amount") amount:Double
    ):Response<CharityDonate>


    //cancel order
    @GET("{type}/cancel_options")
    suspend fun cancelOrder(
        @HeaderMap headers: Map<String, String>,
        @Path("type") type:String
    ):Response<CancelOrder>

    @POST("{type}/cancel/{id}")
    suspend fun cancelOrderPost(
        @HeaderMap headers: Map<String, String>,
        @Path("type") type:String,
        @Path("id") id:Int,
        @Body cancelBody: CancelBody
    ):Response<CancelOrderPost>

    //dialog complaint
    @GET("{type}/complaints_to_driver")
    suspend fun getDriverComplaintOptions(
        @HeaderMap headers: Map<String, String>,
        @Path("type") type: String
    ):Response<DialogComplaintModel>

    @FormUrlEncoded
    @POST("make_complaint_to_driver/{id}")
    suspend fun makeComplaintToDriver(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int,
        @Field("driver_id") driver_id:Int,
        @Field("reason") reason:String,
        @Field("comment") comment:String
    ):Response<MakeComplaintDriverResponseModel>

    //driver rating
    @GET("{order_type}/driver_rating/options")
    suspend fun getDriverRatingOptions(
        @HeaderMap headers: Map<String, String>,
        @Path("order_type") orderType: String,
    ):Response<DriverRatingOptions>

    @POST("driver_rating/{driverId}")
    suspend fun driverRatingPost(
        @HeaderMap headers: Map<String, String>,
        @Path("driverId") id:Int,
        @Body driverRatingPost:DriverRatingPost
    ):Response<DriverRatingPostResponse>


    //CITY API ================================================

    @POST("city/route_tariffs")
    suspend fun cityTariffItems(
        @HeaderMap headers: Map<String, String>,
        @QueryMap map: Map<String, String>
    ):Response<CityTariffMainModel>


    @FormUrlEncoded
    @POST("city/new_order")
    suspend fun cityNewOrder(
        @HeaderMap headers: Map<String, String>,
        @FieldMap map: Map<String, String>,
        @Field("tariff") tariff: String,
        @Field("has_overhead_luggage") has_overhead_luggage: Int,
        @Field("has_conditioner") has_conditioner: Int,
        @Field("for_another") for_another: Int,
        @Field("for_another_phone_number") phone_number: String,
        @Field("receiver_phone_number") receiver_phone_number:String,
        @Field("receiver_comment") receiver_comment:String,
        @Field("used_bonus") used_bonus:Int,
        @Field("used_bonus_amount") used_bonus_amount:Double,
        @Field("order_amount") order_amount:Double,
        @Field("payment_type") payment_type:String,
        @Field("comment") comment:String,
        @Field("card_id") card_id:Int,
        @Field("cargo_type") cargo_type:String
    ):Response<CityCreateNewOrder>



    //


    //MED Turizm clinics
    @GET("med_tourism/clinics")
    suspend fun getClinics(
        @HeaderMap headers: Map<String, String>,
        @Query("q") name:String,
        @Query("category_id") category_id:Int,
        @Query("city_id") city_id:Int,
        @Query("page") page:Int
    ):Response<ClinicsMainModel>

    @GET("med_tourism/clinics/categories")
    suspend fun getClinicsCategories(
        @HeaderMap headers: Map<String, String>
    ):Response<ClinicsCategoriesModel>

    @GET("med_tourism/clinic/{id}")
    suspend fun getClinicView(
        @HeaderMap headers: Map<String, String>,
        @Query("id") id:Int
    ):Response<ClinicMainModel>

    @POST("med_tourism/{clinic}/{id}/add_to_favorites")
    suspend fun clinicAddToFavourite(
        @HeaderMap headers: Map<String, String>,
        @Path("clinic") name: String,
        @Path("id") id:Int
    ):Response<ClinicAddToFavouriteModel>

    @FormUrlEncoded
    @POST("med_tourism/{clinic}/{id}/rate")
    suspend fun medTourismClinicRate(
        @HeaderMap headers: Map<String, String>,
        @Path("clinic") name: String,
        @Path("id") id:Int,
        @Field("rating") rating:Int,
        @Field("comment") comment:String
    ):Response<ClinicAddToFavouriteModel>

    @GET("med_tourism/ambulance")
    suspend fun ambulance(
        @HeaderMap headers: Map<String, String>
    ):Response<AmbulanceModel>


    @GET("med_tourism/doctors")
    suspend fun getDoctors(
        @HeaderMap headers: Map<String, String>,
        @Query("q") name:String,
        @Query("category_id") category_id:Int,
        @Query("city_id") city_id:Int,
        @Query("page") page:Int
    ):Response<ClinicsMainModel>

    @GET("med_tourism/doctors/categories")
    suspend fun getDoctorsCategories(
        @HeaderMap headers: Map<String, String>
    ):Response<ClinicsCategoriesModel>


    @GET("med_tourism/doctor/{id}")
    suspend fun getDoctorView(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id:Int
    ):Response<DoctorViewMainModel>

//    @POST("med_tourism/doctor/{id}/add_to_favorites")
//    suspend fun doctorAddToFavourite(
//        @HeaderMap headers: Map<String, String>,
//        @Path("id") id:Int
//    ):Response<ClinicAddToFavouriteModel>
//
//    @FormUrlEncoded
//    @POST("med_tourism/doctor/{id}/rate")
//    suspend fun medTourismDoctorRate(
//        @HeaderMap headers: Map<String, String>,
//        @Path("id") id:Int,
//        @Field("rating") rating:Int,
//        @Field("comment") comment:String
//    ):Response<ClinicAddToFavouriteModel>

    @GET("med_tourism")
    suspend fun getMedTurIndex(
        @HeaderMap headers: Map<String, String>
    ):Response<MedTurMainModel>

    @GET("{med_tourism}/favorites")
    suspend fun getFavourites(
        @HeaderMap headers: Map<String, String>,
        @Path("med_tourism") medOrTour:String,
        @Query("page") page:Int
    ):Response<FavouritesModel>

    @FormUrlEncoded
    @POST("{med_tourism}/favorites/{id}/delete")
    suspend fun deleteFromFavourites(
        @HeaderMap headers: Map<String, String>,
        @Path("med_tourism") medOrTour:String,
        @Path("id") itemId:Int,
        @Field("type") type: String
    ):Response<ClinicAddToFavouriteModel>



    //=======================================Tourism

    @GET("tourism")
    suspend fun getTourismIndex(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismMainIndex>


    @GET("tourism/tour_packages/{location}")
    suspend fun getIndexUzb(
        @HeaderMap headers: Map<String, String>,
        @Path("location") location: String,
        @Query("q") query:String,
        @Query("country_id") country_id: Int,
        @Query("currency_id") currency_id: Int,
        @Query("sort") sort:String,
        @Query("page") page:Int
    ):Response<IndexUzbModel>

    @GET("tourism/tour_package/{packageId}")
    suspend fun tourPackageView(
        @HeaderMap headers: Map<String, String>,
        @Path("packageId") location: Int
    ):Response<PackageViewMainModel>

    @POST("tourism/tour_package/{id}/add_to_favorites")
    suspend fun addToFavTour(
        @HeaderMap headers: Map<String, String>,
        @Path("packageId") location: Int
    ):Response<ClinicAddToFavouriteModel>

    @GET("tourism/explore")
    suspend fun getTourExplore(
        @HeaderMap headers: Map<String, String>
    ):Response<TourExploreModel>

    @GET("tourism/explore/{id}/packages")
    suspend fun exploreCountryPackages(
        @HeaderMap headers: Map<String, String>,
        @Path("id") location: Int,
        @Query("page") page:Int
    ):Response<ExploreCountryPackage>

    @GET("tourism/explore/{id}")
    suspend fun exploreCountryView(
        @HeaderMap headers: Map<String, String>,
        @Path("id") location: Int
    ):Response<ExploreCountryView>

    @GET("tourism/countries")
    suspend fun getAllCountries(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismCountries>
    @GET("tourism/currencies")
    suspend fun getCurrencies(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismCountries>

    /*==================CAR RENT=================*/

    @GET("tourism/rent_car")
    suspend fun getCarRentIndexMain(
        @HeaderMap headers: Map<String, String>
    ):Response<CarRentIndexMain>

    @GET("tourism/rent_car/cars")
    suspend fun getCarRentCars(
        @HeaderMap headers: Map<String, String>,
        @Query("q") query:String,
        @Query("country_id") country_id: Int,
        @Query("currency_id") currency_id: Int,
        @Query("sort") sort:String,
        @QueryMap markId: Map<String, String>?,
        @Query("car_id") car_id:Int,
        @Query("page") page:Int
    ):Response<CarRentCarsModel>

    @GET("tourism/rent_car/companies")
    suspend fun getCarCompanies(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismCountries>

    @GET("tourism/rent_car/models")
    suspend fun getCarModels(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismCountries>

    @GET("tourism/rent_car/markas")
    suspend fun getCarMarkas(
        @HeaderMap headers: Map<String, String>
    ):Response<TourismCountries>

    @GET("tourism/rent_car/car/{id}")
    suspend fun getCarView(
        @HeaderMap headers: Map<String, String>,
        @Query("id") carId:Int
    ):Response<CarViewModel>

    @POST("tourism/rent_car/{id}/add_to_favorites")
    suspend fun carRentAddToFavourites(
        @HeaderMap headers: Map<String, String>,
        @Path("packageId") location: Int
    ):Response<ClinicAddToFavouriteModel>

    /*===========HOTELS ===============*/


    @GET("tourism/hotels")
    suspend fun getHotelIndex(
        @HeaderMap headers: Map<String, String>,
        @Query("q") query:String,
        @Query("country_id") country_id: Int,
        @Query("currency_id") currency_id: Int,
        @Query("sort") sort:String,
        @Query("page") page:Int
    ):Response<HotelIndex>




}
