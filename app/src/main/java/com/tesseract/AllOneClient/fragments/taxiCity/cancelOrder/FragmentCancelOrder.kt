package com.tesseract.AllOneClient.fragments.taxiCity.cancelOrder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.taxiCity.CancelOrderAdapter
import com.tesseract.AllOneClient.databinding.FragmentCityCancelTaxiBinding
import com.tesseract.AllOneClient.model.taxiCity.cancelOrderPost.CancelBody
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCancelOrder: Fragment(R.layout.fragment_city_cancel_taxi), CancelOrderAdapter.CancelOrderListener {
    private lateinit var binding: FragmentCityCancelTaxiBinding
    private lateinit var viewModel: CancelOrderViewModel
    val args:FragmentCancelOrderArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityCancelTaxiBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CancelOrderViewModel::class.java)
        return binding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.getCancelOrderOptions(headerMapUniversal(requireContext()), args.type)
        binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.cancelOrder.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter=CancelOrderAdapter(it.content, this)
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancel.setOnClickListener {
            val comment=binding.comment.text?.toString()
            if (comment != null) {
                Toast.makeText(context, "delete"+args.type+args.id, Toast.LENGTH_SHORT).show()
                val cancelBody=CancelBody(
                    reason_,
                    comment
                )
                viewModel.cancelOrderPostData(headerMapUniversal(requireContext()), args.type, args.id, cancelBody)
            }
        }

        viewModel.cancelOrderPost.observe(viewLifecycleOwner, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        })
    }


    private var reason_=""

    override fun onItemClick(reason: String) {
        reason_=reason

    }
}