package com.tesseract.AllOneClient.fragments.main.home.HomeMain

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialElevationScale
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.home.HomeOrdersAdapter
import com.tesseract.AllOneClient.adapter.order.ActiveOrderAdapter
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentHomeBinding
import com.tesseract.AllOneClient.dialogs.parcel.ModalDialogParcelSelection
import com.tesseract.AllOneClient.fragments.order.orderHome.OrderViewModel
import com.tesseract.AllOneClient.model.home.HomeOrderModel
import com.tesseract.AllOneClient.model.home.interAreaOrderHistoryModel.OrderHistoryDataListModel
import com.tesseract.AllOneClient.model.home.news.NewsItemModel
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_city_situation.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),
    ModalDialogParcelSelection.ClickListener, HomeOrdersAdapter.OnItemClickListener {

    private lateinit var homeOrderModel: List<HomeOrderModel>
    private var _fragmentHomeBinding: FragmentHomeBinding?=null
    private val fragmentHomeBinding get() = _fragmentHomeBinding!!

    private lateinit var activeOrderAdapter: ActiveOrderAdapter
    private lateinit var viewModel: OrderViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentHomeBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        requireActivity().statusBarColor(
            ResourcesCompat.getColor(resources, R.color.green, requireActivity().theme),
            ResourcesCompat.getColor(resources, R.color.green, requireActivity().theme),
            false
        )
        return fragmentHomeBinding.root
    }

    @SuppressLint("SetTextI18n", "WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        postponeEnterTransition()
//        view.doOnPreDraw { startPostponedEnterTransition() }
        exitTransition=null
        reenterTransition=null

        val drawerLayout:DrawerLayout=requireActivity().findViewById(R.id.drawerLayout)

        fragmentHomeBinding.drawerIcon.setOnClickListener {
            if(!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(Gravity.START)
            else drawerLayout.closeDrawer(Gravity.END);
            drawerLayout.openDrawer(Gravity.START)
        }

        Common.countPageMain = 1
        viewModel.startMain(headerMapUniversal(requireContext()))

        loadItems()
        fragmentHomeBinding.apply {
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            activeOrderAdapter = ActiveOrderAdapter(ArrayList(), requireContext(), true)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = activeOrderAdapter

            recyclerView.addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.activeNextMain(headerMapUniversal(requireContext()))
                }
            })
            balanceText.text = SaveData.getBalance(requireContext())
            txtNameField.text =
                getString(R.string.welcome_A) + " " + (SaveData.getName(requireContext())
                    ?.split(" ")
                    ?.get(0))


            recyclerViewOrders.layoutManager=GridLayoutManager(context,2)
            recyclerViewOrders.adapter=HomeOrdersAdapter(homeOrderModel, this@HomeFragment, requireContext())
        }
        setNews()
        fragmentHomeBinding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        clickListeners()
        activeOrders()
    }

    private fun clickListeners() {
        fragmentHomeBinding.apply {

            allNews.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToFragmentAllNews()
                findNavController().navigate(action)
            }
            showMore.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToOrderFragment()
                findNavController().navigate(action)
            }
        }

    }

    private fun loadItems(){
        homeOrderModel= listOf(
            HomeOrderModel(getString(R.string.inside_of_city), R.drawable.ic_tour_avto_procat),
            HomeOrderModel(getString(R.string.send_post), R.drawable.ic_box_3),
            HomeOrderModel(getString(R.string.taxi_region), R.drawable.ic_tour_uzb),
            HomeOrderModel(getString(R.string.med_turizm), R.drawable.med_turizm_image),
            HomeOrderModel(getString(R.string.international_taxi), R.drawable.taxi_international),
            HomeOrderModel(getString(R.string.tourism), R.drawable.turism_image),
        )
    }

    private lateinit var newsItemModel: NewsItemModel
    private fun setNews() {
        homeViewModel.newsItemModel(headerMapUniversal(requireContext()))
        homeViewModel.responseMessage.observe(viewLifecycleOwner, Observer {
            newsItemModel = it
            fragmentHomeBinding.apply {
                Picasso.get().load(it.image).into(newsImage)
                time.text = it.date
                title.text = it.title
            }
        })

        fragmentHomeBinding.apply {
            newsCardView.setOnClickListener {
//                exitTransition = MaterialElevationScale(false).apply {
//                    duration = 250.toLong()
//                }
//                reenterTransition = MaterialElevationScale(true).apply {
//                    duration = 250.toLong()
//                }

                val direction: NavDirections =
                    HomeFragmentDirections.actionHomeFragmentToFragmentNewsView2(
                        newsItemModel.image!!,
                        newsItemModel.title!!,
                        newsItemModel.description!!,
                        newsItemModel.id!!,
                        true,
                        newsItemModel.date!!
                    )
                val extras = FragmentNavigatorExtras(
                    newsCardView to "cardViewTransition${newsItemModel.id}"
                )
                findNavController().navigate(direction, extras)

            }
        }

    }


    private fun activeOrders() {
        val arrayList = ArrayList<OrderHistoryDataListModel>()
        viewModel.activeOrdersMain.observe(requireActivity(), {

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

    override fun onStart() {
        super.onStart()
        activity?.window?.navigationBarColor = context?.getColor(R.color.white)!!
    }


    override fun parcelType(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToFragmentPostServiceSelection()
        Common.startRegion=""
        Common.startDistrict=""
        Common.endRegion=""
        Common.endDistrict=""
        Common.destination=-1
        Common.startRegionId=""
        Common.startDistrictId=""
        Common.endRegionId=""
        Common.endDistrictId=""
        findNavController().navigate(action)
    }



    override fun onItemClick(position: Int) {
        when (position) {
            0 -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFragmentCityMap()
                findNavController().navigate(action)
            }
            1 -> {
                val addPhotoBottomDialogFragment = ModalDialogParcelSelection(this@HomeFragment)
                addPhotoBottomDialogFragment.show(
                    parentFragmentManager,
                    tag
                )
            }
            2 -> {

                val directions = HomeFragmentDirections.actionHomeFragmentToFragmentTaxiRegions()
                Common.startRegion=""
                Common.startDistrict=""
                Common.endRegion=""
                Common.endDistrict=""
                Common.destination=-1
                Common.startRegionId=""
                Common.startDistrictId=""
                Common.endRegionId=""
                Common.endDistrictId=""
                findNavController().navigate(directions)

            }
            3 -> {

                val action = HomeFragmentDirections.actionHomeFragmentToFragmentMainClinic()
                findNavController().navigate(action)

            }
            4 -> {
                val action=HomeFragmentDirections.actionGlobalChat(1)
                findNavController().navigate(action)

            }
            5 -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFragmentTourismMain()
                findNavController().navigate(action)
            }
        }
    }

}