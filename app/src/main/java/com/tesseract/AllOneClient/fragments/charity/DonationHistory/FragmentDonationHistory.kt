package com.tesseract.AllOneClient.fragments.charity.DonationHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tesseract.AllOneClient.Common.Common
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.charity.GoodHistoryAdapter
import com.tesseract.AllOneClient.databinding.FragmentDonationHistoryBinding
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime
import com.tesseract.AllOneClient.dialogs.main.DialogShowTime2
import com.tesseract.AllOneClient.model.charity.history.Data
import com.tesseract.AllOneClient.pagination.EndlessRecyclerViewScrollListener
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDonationHistory : Fragment() , DialogShowTime.OnDaySelectListener, DialogShowTime2.OnDaySelectListener{
    var binding:FragmentDonationHistoryBinding?=null
    private lateinit var goodHistoryAdapter: GoodHistoryAdapter
    private lateinit var viewModel: DonationHistoryViewModel
    private var tabPosition: Int = 0
    private var startTime: String = ""
    private var endTime: String = ""
    private lateinit var layoutManager:LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDonationHistoryBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(DonationHistoryViewModel::class.java)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Common.donationCountPage = 1
        viewModel.startMain(headerMapUniversal(requireContext()), "all", startTime, endTime)
        layoutManager=LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding?.apply {
            recyclerView.layoutManager=layoutManager
            goodHistoryAdapter= GoodHistoryAdapter( ArrayList(), requireContext())
            recyclerView.adapter=goodHistoryAdapter

            recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.historyData(headerMapUniversal(requireContext()), "all", startTime, endTime)
                }

            })

            viewModel.data.observe(viewLifecycleOwner, {

            })

            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.all_time)))
            tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fromTheTrip)))

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tabPosition= tab?.position!!
                    if (tab.position == 0) {
                        Common.donationCountPage = 1
                        viewModel.startMain(headerMapUniversal(requireContext()), "all", startTime, endTime)
                        goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())
                        recyclerView.adapter=goodHistoryAdapter
                        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                viewModel.historyData(headerMapUniversal(requireContext()), "all", startTime, endTime)
                            }
                        })

                    } else {
                        Common.donationCountPage = 1
                        viewModel.startMain(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                        goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())
                        recyclerView.adapter=goodHistoryAdapter
                        recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                                viewModel.historyData(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                            }

                        })
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

            })

            backToHome.setOnClickListener {
                findNavController().popBackStack()
            }
            orderHistory()

           datePicker1.setOnClickListener {
                DialogShowTime(getString(R.string.daparture_date), this@FragmentDonationHistory).show(
                    parentFragmentManager,
                    tag
                )
            }
           datePicker2.setOnClickListener {
                DialogShowTime2(getString(R.string.daparture_date), this@FragmentDonationHistory).show(
                    parentFragmentManager,
                    tag
                )
            }

        }
    }

    override fun selectDayListener(time: String) {
        binding?.date1?.text = time
        binding?.recyclerView?.invalidate()
        startTime = time
        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            if (tabPosition==1){
                Common.donationCountPage = 1
                viewModel.startMain(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())

                binding!!.recyclerView.adapter=goodHistoryAdapter
                binding!!.recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        viewModel.historyData(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                    }
                })
            }else if (tabPosition==0){
                Common.donationCountPage = 1
                binding?.recyclerView?.invalidate()
                viewModel.startMain(headerMapUniversal(requireContext()), "all",  startTime, endTime)
                goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())
                val layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding!!.recyclerView.layoutManager=layoutManager
                binding!!.recyclerView.adapter=goodHistoryAdapter
                binding!!.recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        viewModel.historyData(headerMapUniversal(requireContext()), "all",  startTime, endTime)
                    }

                })
            }
        }
    }

    override fun selectDayListener2(time: String) {
        binding?.date2?.text = time
        endTime = time
        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            if (tabPosition==1){
                Common.donationCountPage = 1
                viewModel.startMain(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())
                binding!!.recyclerView.adapter=goodHistoryAdapter
                binding!!.recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        viewModel.historyData(headerMapUniversal(requireContext()), "from_trips", startTime, endTime)
                    }
                })
            }else if (tabPosition==0){
                Common.donationCountPage = 1
                viewModel.startMain(headerMapUniversal(requireContext()), "all",  startTime, endTime)
                goodHistoryAdapter= GoodHistoryAdapter(ArrayList(), requireContext())
                binding!!.recyclerView.adapter=goodHistoryAdapter
                binding!!.recyclerView.addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager){
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        viewModel.historyData(headerMapUniversal(requireContext()), "all",  startTime, endTime)
                    }

                })
            }
        }
    }



    private fun orderHistory() {
        val arrayList = ArrayList<Data>()
        viewModel.data.observe(viewLifecycleOwner, {
            arrayList.addAll(it)
            if (arrayList.size!=0){
                goodHistoryAdapter.addList(arrayList)
            }
            arrayList.clear()
        })

    }

}