package com.tesseract.AllOneClient.fragments.order.orderHome

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.order.ActiveOrderAdapter
import com.tesseract.AllOneClient.adapter.order.OrderHistoryAdapter
import com.tesseract.AllOneClient.databinding.FragmentOrderBinding
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime2
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryDataListModel
import com.tesseract.AllOneClient.model.order.MessageEvent
import com.tesseract.AllOneClient.model.order.MessageEventActiveOrder
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class OrderFragment : Fragment(R.layout.fragment_order),
    DialogShowTime.OnDaySelectListener,
    DialogShowTime2.OnDaySelectListener {
    private var _fragmentOrderBinding: FragmentOrderBinding? = null
    private val fragmentOrderBinding get() = _fragmentOrderBinding!!
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    private lateinit var activeOrderAdapter: ActiveOrderAdapter
    private var startTime: String = ""
    private var endTime: String = ""
    private var isCurrentFragment: Boolean = true
    private var tabPosition: Int = 0

    private lateinit var viewModel: OrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentOrderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        return fragmentOrderBinding!!.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Common.countPage = 1
        Common.orderHistoryCountPage = 1

        viewModel.start(headerMapUniversal(requireContext()))
        viewModel.startOrderHistory(headerMapUniversal(requireContext()), "", "")

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        activeOrderAdapter = ActiveOrderAdapter(ArrayList(), requireContext())
        fragmentOrderBinding.orderTaxiList.layoutManager = layoutManager
        fragmentOrderBinding.orderTaxiList.adapter = activeOrderAdapter
        fragmentOrderBinding.orderTaxiList.setHasFixedSize(true)

        val resId: Int = R.anim.layout_animation
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        fragmentOrderBinding!!.orderTaxiList.layoutAnimation = animation

        fragmentOrderBinding.orderTaxiList.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.activeNext(headerMapUniversal(requireContext()))
            }
        })

        val layoutManager2 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        orderHistoryAdapter = OrderHistoryAdapter(ArrayList(), requireContext())
        fragmentOrderBinding.orderPackageList.layoutManager = layoutManager2
        fragmentOrderBinding.orderPackageList.adapter = orderHistoryAdapter
        fragmentOrderBinding!!.orderPackageList.layoutAnimation = animation
        fragmentOrderBinding.orderPackageList.setHasFixedSize(true)
        fragmentOrderBinding.orderPackageList.addOnScrollListener(object :
            EndlessRecyclerViewScrollListener(layoutManager2) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.historyList(headerMapUniversal(requireContext()), "", "")
            }
        })

        fragmentOrderBinding!!.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        if (isCurrentFragment) {
            activeOrders()
            orderHistory()
        }
        fragmentOrderBinding!!.datePicker1.setOnClickListener {
            DialogShowTime(getString(R.string.daparture_date), this).show(
                parentFragmentManager,
                tag
            )
        }
        fragmentOrderBinding!!.datePicker2.setOnClickListener {
            DialogShowTime2(getString(R.string.daparture_date), this).show(
                parentFragmentManager,
                tag
            )
        }
        fragmentOrderBinding!!.datePickersLayouts.visibility = View.GONE

        fragmentOrderBinding.apply {
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.active)))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.history)))

            if (tabPosition==1){
                tabLayout.getTabAt(1)?.select()
                fragmentOrderBinding!!.datePickersLayouts.visibility = View.VISIBLE
                orderPackageList.visibility = View.VISIBLE
                orderTaxiList.visibility = View.GONE
            }



            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Log.i("tab selectedasrdt", ""+tab?.position)
                    tabPosition= tab?.position!!
                    if (tab.position == 0) {
                        orderPackageList.visibility = View.GONE
                        orderTaxiList.visibility = View.VISIBLE
                        fragmentOrderBinding!!.datePickersLayouts.visibility = View.GONE
                    } else {
                        orderPackageList.visibility = View.VISIBLE
                        orderTaxiList.visibility = View.GONE
                        fragmentOrderBinding!!.datePickersLayouts.visibility = View.VISIBLE
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })
        }


    }

    private fun orderHistory() {

        val arrayList = ArrayList<OrderHistoryDataListModel>()
        viewModel.orderHistory.observe(requireActivity(),  {
            for (i in it.content?.orderHistoryDataData?.indices!!) {
                val orderHistoryList = OrderHistoryDataListModel(
                    it.content?.orderHistoryDataData!![i].id,
                    it.content?.orderHistoryDataData!![i].date,
                    it.content?.orderHistoryDataData!![i].orders,
                )
                arrayList.add(orderHistoryList)
            }
            if (arrayList.size != 0) {
                orderHistoryAdapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }

    private fun activeOrders() {
        val arrayList = ArrayList<OrderHistoryDataListModel>()
        viewModel.activeOrders.observe(requireActivity(), {
            for (i in it.content?.orderHistoryDataData?.indices!!) {
                val orderHistoryList = OrderHistoryDataListModel(
                    it.content?.orderHistoryDataData!![i].id,
                    it.content?.orderHistoryDataData!![i].date,
                    it.content?.orderHistoryDataData!![i].orders,
                )
                arrayList.add(orderHistoryList)
            }
            if (arrayList.size != 0) {
                activeOrderAdapter.addList(arrayList)
            }
            arrayList.clear()
        })
    }

    override fun selectDayListener(time: String) {
        fragmentOrderBinding.date1.text = time
        startTime = time
        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            viewModel.historyList(headerMapUniversal(requireContext()), startTime, endTime)
            orderHistory()
        }
    }

    override fun selectDayListener2(time: String) {
        fragmentOrderBinding.date2.text = time
        endTime = time
        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            viewModel.historyList(headerMapUniversal(requireContext()), startTime, endTime)
            orderHistory()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun messageEvent(event: MessageEventActiveOrder) {
        when {
            event.tariff.toString() == "interarea" -> {
                val action =
                    event.id?.let {
                        event.tariff?.let { it1 ->
                            OrderFragmentDirections.actionGlobalInterareaActiveOrder(
                                it, it1
                            )
                        }
                    }
                if (action != null) {
                    findNavController().navigate(action)
                }
                isCurrentFragment = false
            }
            event.tariff.toString()=="interarea_parcel_delivery" ->{
                val action=
                    OrderFragmentDirections.actionGlobalParcelActiveOrder(event.id!!)
                findNavController().navigate(action)
                isCurrentFragment = false
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when {
            event.tariff.toString() == "interarea_parcel_delivery" -> {
                val action =
                    OrderFragmentDirections.actionOrderFragmentToFragmentOrderParcelAboutTrip(event.id)
                findNavController().navigate(action)
                isCurrentFragment = false
            }
            event.tariff.toString() == "interarea" -> {
                val action =
                    OrderFragmentDirections.actionOrderFragmentToFragmentOrderRegionAboutTrip(event.id)
                findNavController().navigate(action)
                Log.i("region is called", "")
                isCurrentFragment = false
            }
            else -> {
                val action =
                    OrderFragmentDirections.actionOrderFragmentToFragmentOrderCityAboutTrip()
                findNavController().navigate(action)
                Log.i("city is called", "")
                isCurrentFragment = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentOrderBinding=null
    }
}