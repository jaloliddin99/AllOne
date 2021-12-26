package com.tesseract.AllOneClient.fragments.taxiCity.orderFragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.Common.Common.isCityTariffPreviousBackstack
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.taxiCity.CityTariffAdapter
import com.tesseract.AllOneClient.adapter.taxiCity.CityTariffCargoItems
import com.tesseract.AllOneClient.adapter.taxiCity.CityTariffLargeItemAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentCityTariffsBinding
import com.tesseract.AllOneClient.dialogs.city.CityReceiverModalDialog
import com.tesseract.AllOneClient.dialogs.city.ViewPagerDialog
import com.tesseract.AllOneClient.dialogs.sockets.DialogOrderCancelled
import com.tesseract.AllOneClient.fragments.order.mapActiveRegion.MapActivityRegionViewModel
import com.tesseract.AllOneClient.model.order.MapActiveRegionModel.RoutindDetails
import com.tesseract.AllOneClient.model.taxiCity.CityShareCardBonusModel
import com.tesseract.AllOneClient.model.taxiCity.Contact
import com.tesseract.AllOneClient.model.taxiCity.StationModel
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Content
import com.tesseract.AllOneClient.model.taxiCity.tariffs.ListenOrderAccept
import com.tesseract.AllOneClient.model.taxiCity.tariffs.Opt
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.CityShareStationModel
import com.tesseract.AllOneClient.services.SocketHandler
import com.tesseract.AllOneClient.utils.bitmapDescriptorFromVector
import com.tesseract.AllOneClient.utils.dipToPixels
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_city_tariffs.*
import kotlin.properties.Delegates


@AndroidEntryPoint
class FragmentCityTariffs : Fragment(), OnMapReadyCallback,
    CityTariffAdapter.OnImageClickListener,
    CityTariffLargeItemAdapter.OnImageClickListener,
    CityReceiverModalDialog.OnOrderClickListener,
    CityTariffCargoItems.OnCargoSelectListener,
    DialogOrderCancelled.OnLickListener
{
    private lateinit var mMap: GoogleMap

    private var lat: Double = 0.0
    private var long: Double = 0.0
    private var startLat = 0.0
    private var startLong = 0.0
    private var endLat = 0.0
    private var endLong = 0.0
    private val args: FragmentCityTariffsArgs by navArgs()
    private var _binding: FragmentCityTariffsBinding?=null
    private val binding get() = _binding!!

    private lateinit var cityTariffAdapter: CityTariffAdapter
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null
    private var mBottomSheetBehavior2: BottomSheetBehavior<*>? = null

    private lateinit var cityOrderModelDialogAdapter: CityTariffLargeItemAdapter
    private lateinit var viewModel: CityTariffViewModel
    private lateinit var routeViewModel: MapActivityRegionViewModel
    private var locationsLatLng = ArrayList<LatLng>()

    private var stationList = ArrayList<StationModel>()
    private var cityShareCardBonusModel: CityShareCardBonusModel=CityShareCardBonusModel("", "", -1, "", "cash")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCityTariffsBinding.inflate(inflater, container, false)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.darker_color, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.white, requireActivity().theme),
            false
        )
        viewModel = ViewModelProvider(this).get(CityTariffViewModel::class.java)
        routeViewModel = ViewModelProvider(this).get(MapActivityRegionViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        CityTariffAdapter.isMainSelected=true

        recyclerViewController()
        modalDialogController()
        clickListeners()
        updateUI()

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()
        binding.btnGotoSearch.setOnClickListener {
            if (isInProgress){
                createNewOrder()
            }else{
                demodifyUI()
            }
        }


        binding.apply {
            startDestination.setOnClickListener {
                setBackStackData("startDestination", args.startLocation+"###"+args.startName, true)
            }
            endDestination.setOnClickListener {
                setBackStackData("endDestination", args.endLocation+"###"+args.endName, true)
            }

            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
        }



        viewModel.cityNewOrder.observe(viewLifecycleOwner, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            modifyUI()
//            mSocket.emit("chat_send", JSONObject(Gson().toJson(sendMessage)))
//            val sendMessage= ListenOrderAccept("")

            Toast.makeText(context, "${it.content.order_id}", Toast.LENGTH_SHORT).show()

            mSocket.on("client_order_${it.content.order_id}") { args ->
                if (args[0] != null) {
                    val response = args[0] as ListenOrderAccept
                    requireActivity().runOnUiThread {

                        if (response.status=="client_cancelled"){
                            DialogOrderCancelled(this).show(parentFragmentManager, tag)
                        }

                        if (response.status=="driver_appointed"){
                            DialogOrderCancelled(this).show(parentFragmentManager, tag)
                        }

                        if (response.status=="client_cancelled"){
                            DialogOrderCancelled(this).show(parentFragmentManager, tag)
                        }

                        if (response.status=="client_cancelled"){
                            DialogOrderCancelled(this).show(parentFragmentManager, tag)
                        }

                    }
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner,{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private var isInProgress=true
    @SuppressLint("SetTextI18n")
    private fun modifyUI(){
        binding.apply {

            mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
            isInProgress=false
            mBottomSheetBehavior2?.setPeekHeight(dipToPixels(requireContext(), 220f).toInt(), true)
            mBottomSheetBehavior?.setPeekHeight(0, true)
            bottomSheet.visibility=View.GONE


            lottieAnimation.visibility=View.VISIBLE

            btnGotoSearch.text=getString(R.string.cancel)
            btnGotoSearch.backgroundTintList=ContextCompat.getColorStateList(requireContext(), R.color.red)

            lac.tariff.text=content.tariff
            lac.price.text=SaveData.formatPhone(orderAmount.toString())+" "+getString(R.string.summa1)

            lac.pickup.text=args.startName
            lac.toWhere.text=args.endName
            lac.paymentType.text=paymentType

            if (forAnother==1){
                lac.lineOrderForOtherPerson.visibility=View.VISIBLE
                lac.linearOrderForOther.visibility=View.VISIBLE
                lac.orderForOtherOtherPerson.text=forAnotherPhoneNumber
            }else{
                lac.lineOrderForOtherPerson.visibility=View.GONE
                lac.linearOrderForOther.visibility=View.GONE
            }

            if (this@FragmentCityTariffs.hasAirConditioner==1){
                lac.lineConditioner.visibility=View.VISIBLE
                lac.linearConditioner.visibility=View.VISIBLE
            }else{
                lac.lineConditioner.visibility=View.GONE
                lac.linearConditioner.visibility=View.GONE
            }

            if (this@FragmentCityTariffs.comment.isEmpty()){
                lac.linearComment.visibility=View.GONE
            }else{
                lac.comment.text=this@FragmentCityTariffs.comment
                lac.linearComment.visibility=View.VISIBLE
            }
        }

    }
    private fun demodifyUI(){
        isInProgress=true
        mBottomSheetBehavior2?.setPeekHeight(dipToPixels(requireContext(), 0f).toInt(), true)
        mBottomSheetBehavior?.setPeekHeight(dipToPixels(requireContext(), 400f).toInt(), true)
        bottomSheet.visibility=View.VISIBLE


        lottieAnimation.visibility=View.GONE

        binding.btnGotoSearch.text=getString(R.string.orders)
        binding.btnGotoSearch.backgroundTintList=ContextCompat.getColorStateList(requireContext(), R.color.green)
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16f))

        val latLngBounds:LatLngBounds.Builder=LatLngBounds.Builder()
        for (i in map.indices){
            val latLng=LatLng(map[i].split(",")[0].toDouble(), map[i].split(",")[1].toDouble())
            latLngBounds.include(latLng)
        }


        val cameraUpdate:CameraUpdate=CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), dipToPixels(requireContext(), 30f).toInt())
        mMap.animateCamera(cameraUpdate, 10, object :GoogleMap.CancelableCallback{
            override fun onFinish() {
            }
            override fun onCancel() {
            }

        })

    }


    private lateinit var contentList: List<Content>
    private var forAnother=0
    private var forAnotherPhoneNumber=""
    private var receiverPhoneNum=""
    private var receiverComment=""
    private var usedBonus=0
    private var usedBonusAmount=0.0
    private var orderAmount=0.0
    private var paymentType="cash"
    private var comment=""
    private var cargoType="no"
    var hasOverheadLuggage=0
    var hasAirConditioner=1

    private fun createNewOrder(){
        hasOverheadLuggage=if (binding.hasAirConditioner.isChecked){
            1
        }else{
            0
        }
        hasAirConditioner=if (binding.hasAirConditioner.isChecked){
            1
        }else{
            0
        }

        orderAmount=content.price.toDouble()
        if (hasAirConditioner==1){
            orderAmount=content.price_with_ac.toDouble()
        }
        if (usedBonus==1){
            orderAmount -= usedBonusAmount
        }

        if (content.tariff=="parcel_delivery"){
            CityReceiverModalDialog(this@FragmentCityTariffs).show(parentFragmentManager, tag)
            return
        }

        if (content.tariff=="cargo"){
            if (cargoType=="no"){
                Toast.makeText(context, "Please, select the cargo type", Toast.LENGTH_SHORT).show()
                return
            }
        }


        Log.i(TAG, "createNewOrder: tariff ${content.tariff}\n" +
                "hasOverheadLuggage $hasOverheadLuggage, \n" +
                "hasAirConditioner $hasAirConditioner, \n" +
                "forAnother $forAnother, \n" +
                "forAnotherPhoneNumber $forAnotherPhoneNumber, \n" +
                "receiverPhoneNum $receiverPhoneNum, \n" +
                "usedBonus $usedBonus, \n" +
                "usedBonusAmount $usedBonusAmount, \n" +
                "orderAmount $orderAmount, \n" +
                "paymentType $paymentType, \n" +
                "comment $comment, \n" +
                "cityShareCardBonusModel.cardId ${ cityShareCardBonusModel.cardId} \n" +
                "cargoType $cargoType")

        viewModel.cityNewOrderPost(
            headerMapUniversal(requireContext()),
            map,
            content.tariff,
            hasOverheadLuggage,
            hasAirConditioner,
            forAnother,
            forAnotherPhoneNumber,
            receiverPhoneNum,
            receiverComment,
            usedBonus,
            usedBonusAmount,
            orderAmount,
            paymentType,
            comment,
            cityShareCardBonusModel.cardId,
            cargoType,
        )
    }



    val map =ArrayList<String>()

    private var isStartGiven by Delegates.notNull<Boolean>()
    private var isEndGiven by Delegates.notNull<Boolean>()
    private fun prepareRoutingRequest() {
        isStartGiven = false
        isEndGiven = false

        map.clear()

        if (args.startLocation.isNotEmpty()) {
            map.add(args.startLocation)
            isStartGiven = true
        }

        if (args.endLocation.isNotEmpty()) {
            map.add(args.endLocation)
            isEndGiven = true
        }

        viewModel.cityTariffMainModel(headerMapUniversal(requireContext()), map)
        if (isStartGiven && isEndGiven) {
            startLat = args.startLocation.split(",")[0].toDouble()
            startLong = args.startLocation.split(",")[1].toDouble()
            endLat = args.endLocation.split(",")[0].toDouble()
            endLong = args.endLocation.split(",")[1].toDouble()

            lat = (startLat + endLat) / 2
            long = (endLong + startLong) / 2

            val list = ArrayList<Double>()
            list.add(startLong)
            list.add(startLat)

            val listEnd = ArrayList<Double>()
            listEnd.add(endLong)
            listEnd.add(endLat)
            val listBig = ArrayList<ArrayList<Double>>()
            listBig.add(list)
            listBig.add(listEnd)

            val routingModel = RoutindDetails(false, listBig)

            routeViewModel.mapGetRouting(headerMapUniversal(requireContext()), routingModel)
        }

    }

    private lateinit var bottomSheet:View
    private fun modalDialogController() {


        binding.wishes.setOnClickListener {
            mBottomSheetBehavior?.state=BottomSheetBehavior.STATE_EXPANDED
        }

        bottomSheet = requireView().findViewById(R.id.bottomSheetNestedScrollView)
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        (mBottomSheetBehavior as BottomSheetBehavior<*>).setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.stateExpand.alpha = 0f
                        binding.layoutLinear.alpha = 1f

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.stateExpand.alpha = 1f
                        binding.layoutLinear.alpha = 0f

                    }
                }
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {
                binding.bottomSheetBackgroundLayer.alpha = slideOffset
                if (slideOffset <= 0.5) {
                    binding.layoutLinear.alpha = 2 * (0.5f - slideOffset)
                    binding.layoutLinear.elevation = 50 * 2 * (0.5f - slideOffset)
                } else {
                    binding.stateExpand.elevation = 50 * 2 * (slideOffset - 0.5f)
                    binding.stateExpand.alpha = 2 * (slideOffset - 0.5f)

                }
            }
        })


        val bottomSheet2: View = requireView().findViewById(R.id.bottomSheetNestedScrollView2)
        mBottomSheetBehavior2 = BottomSheetBehavior.from(bottomSheet2)
        (mBottomSheetBehavior2 as BottomSheetBehavior<*>).setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                }
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {

            }
        })


        binding.stateExpand.alpha = 0f
        binding.layoutLinear.alpha = 1f

    }



    @SuppressLint("SetTextI18n")
    private fun recyclerViewController() {


        binding.apply {

            recyclerCarImages.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            viewModel.cityTariffMainModelObserver.observe(viewLifecycleOwner, {
                arrivalTime.text=it.content[Common.cityTariffRecyclerView].arrival_time
                cityTariffAdapter = CityTariffAdapter(Common.cityTariffRecyclerView, it.content, this@FragmentCityTariffs, requireContext())
                recyclerCarImages.scrollToPosition(Common.cityTariffRecyclerView)
                recyclerCarImages.adapter = cityTariffAdapter
                recyclerCarImages.setHasFixedSize(true)

                content=it.content[Common.cityTariffRecyclerView]

                contentList=it.content

                cityOrderModelDialogAdapter = CityTariffLargeItemAdapter(it.content, this@FragmentCityTariffs, requireContext())

                recyclerBigCarImages.adapter = cityOrderModelDialogAdapter
                recyclerBigCarImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                recyclerBigCarImages.scrollToPosition(Common.cityTariffRecyclerView)
                recyclerBigCarImages.setHasFixedSize(true)
                indicator.attachToRecyclerView(recyclerBigCarImages)


            })

            recyclerViewCargo.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            recyclerBigCarImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val position = getCurrentItem()

                        CityTariffAdapter.isMainSelected=true

                        content=contentList[position]
                        Common.cityTariffRecyclerView=position

                        cityTariffAdapter = CityTariffAdapter(Common.cityTariffRecyclerView, contentList, this@FragmentCityTariffs, requireContext())
                        recyclerCarImages.scrollToPosition(Common.cityTariffRecyclerView)
                        recyclerCarImages.adapter = cityTariffAdapter

                        arrivalTime.text=content.arrival_time

                        if (content.tariff=="parcel_delivery"){
                            parcel.visibility=View.VISIBLE
                        }else{
                            parcel.visibility=View.GONE
                        }
                        if (content.tariff=="peregon"||content.tariff=="accumulator"||content.tariff=="for_products"||content.tariff=="parcel_delivery"
                            ||content.tariff=="peregon"){
                            binding.airConditioner.visibility=View.GONE
                        }else{
                            binding.airConditioner.visibility=View.VISIBLE
                        }
                        if (content.tariff=="cargo"){
                            recyclerViewCargo.visibility=View.VISIBLE
                            recyclerViewCargo.adapter=CityTariffCargoItems(content.opt, this@FragmentCityTariffs)
                            recyclerViewCargo.setHasFixedSize(true)
                        }else{
                            recyclerViewCargo.visibility=View.GONE
                        }

                    }
                }
            })

            recyclerBigCarImages.onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(recyclerBigCarImages)

            if (args.endLocation.isEmpty()){
                addNewStation.visibility=View.GONE
            }

            addNewStation.setOnClickListener {
                if (isStartGiven && isEndGiven) {
                    val cityShareModel = CityShareStationModel(
                        stationModel = stationList
                    )
                    val action = FragmentCityTariffsDirections.actionGlobalCityStations(
                        args.startLocation,
                        args.startName, args.endLocation, args.endName,
                        cityShareModel
                    )
                    findNavController().navigate(action)
                }
            }

            getBackStackData<Contact>("selectedContact", true){

                if (it.isCancelled){
                    forAnother=0
                }else{
                    receiverNameText.text=it.name
                    forAnother=1
                    contact=it
                    forAnotherPhoneNumber=it.numbers[0].replace(" ", "")
                }

                restoreState()
            }

            getBackStackData<String>("commentKey", true){
                commentType.setText(it)
                this@FragmentCityTariffs.comment=it

                restoreState()
            }

            getBackStackData<CityShareCardBonusModel>("FragmentCityPaymentMethod", true){
                cityShareCardBonusModel=it
                cardNumAndType.text=it.cardType+" **** " +it.cardNumber.substring(
                    it.cardNumber.length-4, it.cardNumber.length)


                if (it.bonusAmount.toDouble()==0.0){
                    usedBonus=0
                }else{
                    usedBonus=1
                    usedBonusAmount=it.bonusAmount.toDouble()
                }


                paymentType=it.paymentType

                Log.i(TAG, "FragmentCityPaymentMethod: paymentType ${it.paymentType},\n" +
                        " bonusAmount ${it.bonusAmount},\n" +
                        "cardId ${it.cardId},\n" +
                        " cardNumber ${it.cardNumber},\n" +
                        "cardType ${it.cardType}")

                restoreState()

            }

            commentForDriver.setOnClickListener {
                val action=FragmentCityTariffsDirections.actionGlobalAddComment()
                findNavController().navigate(action)
            }
        }

    }

    private var contact: Contact?=null

    private fun updateUI() {
        binding.apply {
            startDestination.text = args.startName
            endDestination.text = args.endName
        }
    }

    private fun decodeToLatLng(list: List<List<Double>>) {
        val latLngList = ArrayList<LatLng>()
        for (i in list.indices) {
            if (i != list.lastIndex) {
                val lng: Double = list[i][0]
                val lat: Double = list[i][1]
                latLngList.add(LatLng(lat, lng))
            }
        }
        mMap.clear()
        drawRoute(latLngList)
    }

    private fun drawRoute(list: ArrayList<LatLng>) {
        mMap.addPolyline(
            PolylineOptions().addAll(
                list
            ).width(15F).color(Color.parseColor("#15C657")).geodesic(true)
                .jointType(JointType.ROUND)
        )

        val yourLocation = LatLng(lat, long)
        val start = LatLng(startLat, startLong)
        val end = LatLng(endLat, endLong)
        mMap.addMarker(
            MarkerOptions().position(start)
                .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_dest))
        )

        for (i in locationsLatLng.indices) {
            mMap.addMarker(
                MarkerOptions().position(locationsLatLng[i])
                    .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_dest))
            )
        }
        mMap.addMarker(
            MarkerOptions().position(end)
                .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_chess_shape))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation))

    }


    private fun clickListeners() {

        binding.apply {
            orderForOther.setOnClickListener {
                val action = FragmentCityTariffsDirections.actionGlobalContact()
                findNavController().navigate(action)
            }
            paymentMethod.setOnClickListener {
                val action = FragmentCityTariffsDirections.actionFragmentCityOrderMapsToFragmentCityPaymentMethod()
                findNavController().navigate(action)
            }

            paymentTypeM.setOnClickListener {
                val action = FragmentCityTariffsDirections.actionFragmentCityOrderMapsToFragmentCityPaymentMethod()
                findNavController().navigate(action)
            }
        }
    }

    private var previousId=-10
    private lateinit var content:Content
    override fun onItemClick(content: Content, position: Int) {
        this.content=content
        if (content.id==previousId){
            mBottomSheetBehavior?.state=BottomSheetBehavior.STATE_EXPANDED
        }
        previousId=content.id
        Common.cityTariffRecyclerView=position
        binding.recyclerBigCarImages.scrollToPosition(Common.cityTariffRecyclerView)
        arrivalTime.text=content.arrival_time
    }

    override fun onShowViewPager(content: Content) {
        ViewPagerDialog(content).show(parentFragmentManager, "fragment Manager")
    }

    private fun getCurrentItem(): Int {
        return (binding.recyclerBigCarImages.layoutManager as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled = false

        val latLngBounds:LatLngBounds.Builder=LatLngBounds.Builder()
        val startDestination=LatLng(args.startLocation.split(",")[0].toDouble(),args.startLocation.split(",")[1].toDouble() )

        mMap.addMarker(
            MarkerOptions().position(startDestination)
                .icon(requireContext().bitmapDescriptorFromVector(R.drawable.ic_dest)))
        mMap.setPadding(0, 0, 0, dipToPixels(requireContext(), 180f).toInt())


        if (args.endLocation.isNotEmpty()){
            val endDestination=LatLng(args.endLocation.split(",")[0].toDouble(),args.endLocation.split(",")[1].toDouble() )
            latLngBounds.include(endDestination)
            latLngBounds.include(startDestination)

            val cameraUpdate:CameraUpdate=CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), dipToPixels(requireContext(), 30f).toInt())
            mMap.animateCamera(cameraUpdate, 10, object :GoogleMap.CancelableCallback{
                override fun onFinish() {

                }

                override fun onCancel() {

                }

            })
        }else{
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startDestination, 18f))
        }




        getBackStackData<ArrayList<StationModel>>("CityNewStations", true) {
            if (args.endLocation.isNotEmpty()){
                stationList = it
                map.clear()
                map.add(args.startLocation)
                for (i in it.indices) {
                    map.add(it[i].stationLatLng)
                }
                map.add(args.endLocation)
                viewModel.cityTariffMainModel(headerMapUniversal(requireContext()), map)
                val list = ArrayList<Double>()
                list.add(startLong)
                list.add(startLat)

                val listEnd = ArrayList<Double>()
                listEnd.add(endLong)
                listEnd.add(endLat)
                val listBig = ArrayList<ArrayList<Double>>()
                listBig.add(list)
                for (i in it.indices) {
                    val stationList = ArrayList<Double>()
                    val lng = it[i].stationLatLng.split(",")[1].toDouble()
                    val lat = it[i].stationLatLng.split(",")[0].toDouble()
                    locationsLatLng.add(LatLng(lat, lng))
                    stationList.add(lng)
                    stationList.add(lat)
                    listBig.add(stationList)
                }
                listBig.add(listEnd)

                val routingModel = RoutindDetails(false, listBig)

                routeViewModel.mapGetRouting(headerMapUniversal(requireContext()), routingModel)

                restoreState()
            }

        }

        if (!isCityTariffPreviousBackstack) {
            prepareRoutingRequest()
        }

        routeViewModel.locationRouting.observe(viewLifecycleOwner, {
            decodeToLatLng(it)
        })

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap.isMyLocationEnabled = false

    }

    private fun restoreState(){

        if (comment.isNotEmpty()){
            binding.commentType.setText(comment)
        }

        if (paymentType=="cash"){
            binding.cardNumAndType.text=getString(R.string.with_money)
            binding.paymentTypeM.text=getString(R.string.with_money)
        }else if (paymentType=="card"){
            binding.cardNumAndType.text=cityShareCardBonusModel.cardType+" **** " +cityShareCardBonusModel.cardNumber.substring(
                cityShareCardBonusModel.cardNumber.length-4, cityShareCardBonusModel.cardNumber.length)

            binding.paymentTypeM.text=cityShareCardBonusModel.cardType+" **** " +cityShareCardBonusModel.cardNumber.substring(
                cityShareCardBonusModel.cardNumber.length-4, cityShareCardBonusModel.cardNumber.length)

        }

        if (forAnother==1){
            binding.receiverNameText.text=contact?.name
        }
    }



    private fun <T> Fragment.getBackStackData(
        key: String,
        singleCall: Boolean = true,
        result: (T) -> (Unit)
    ) {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                result(it)
                if (singleCall) findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(
                    key
                )
            }
    }

    override fun receiverDetails(phoneNum: String, receiverComment: String) {
        receiverPhoneNum=phoneNum
        this.receiverComment=receiverComment



        Log.i(TAG, "createNewOrder: tariff ${content.tariff}\n" +
                "hasOverheadLuggage $hasOverheadLuggage, \n" +
                "hasAirConditioner $hasAirConditioner, \n" +
                "forAnother $forAnother, \n" +
                "forAnotherPhoneNumber $forAnotherPhoneNumber, \n" +
                "receiverPhoneNum $receiverPhoneNum, \n" +
                "receiverComment ${this.receiverComment}, \n" +
                "usedBonus $usedBonus, \n" +
                "usedBonusAmount $usedBonusAmount, \n" +
                "orderAmount $orderAmount, \n" +
                "paymentType $paymentType, \n" +
                "comment $comment, \n" +
                "cityShareCardBonusModel.cardId ${ cityShareCardBonusModel.cardId} \n" +
                "cargoType $cargoType")

        viewModel.cityNewOrderPost(
            headerMapUniversal(requireContext()),
            map,
            content.tariff,
            hasOverheadLuggage,
            hasAirConditioner,
            forAnother,
            forAnotherPhoneNumber,
            receiverPhoneNum,
            this.receiverComment,
            usedBonus,
            usedBonusAmount,
            orderAmount,
            paymentType,
            comment,
            cityShareCardBonusModel.cardId,
            cargoType,
        )
    }

    private var opt: Opt?=null

    override fun onCargoSelect(opt: Opt) {
        this.opt=opt
        cargoType=opt.type
    }

    override fun orderAgain() {
        createNewOrder()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }


    fun <T> Fragment.setBackStackData(key: String, data: T, doBack: Boolean = false) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, data)
        if (doBack)
            findNavController().popBackStack()
    }



}