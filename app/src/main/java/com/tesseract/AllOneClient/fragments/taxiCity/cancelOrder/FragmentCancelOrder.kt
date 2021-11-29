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
import androidx.core.content.res.ResourcesCompat
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
import com.tesseract.AllOneClient.utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCancelOrder: Fragment(), CancelOrderAdapter.CancelOrderListener {
    private  var _binding: FragmentCityCancelTaxiBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: CancelOrderViewModel
    val args:FragmentCancelOrderArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityCancelTaxiBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CancelOrderViewModel::class.java)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            true
        )
        return binding.root
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        viewModel.getCancelOrderOptions(headerMapUniversal(requireContext()), args.type)
        binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.errorPost.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.errorOptions.observe(viewLifecycleOwner, {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
        viewModel.cancelOrder.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter=CancelOrderAdapter(it.content, this)
            binding.loader.loader.visibility=View.GONE
        })

        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cancel.setOnClickListener {
            val comment=binding.comment.text?.toString()
            if (comment != null) {
                val cancelBody=CancelBody(
                    reason_,
                    comment
                )
                viewModel.cancelOrderPostData(headerMapUniversal(requireContext()), args.type, args.id, cancelBody)
                binding.loader.loader.visibility=View.VISIBLE
            }
        }

        viewModel.cancelOrderPost.observe(viewLifecycleOwner, {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            binding.loader.loader.visibility=View.GONE
            val action=FragmentCancelOrderDirections.actionGlobalComposeFragment()
            findNavController().navigate(action)
        })
    }


    private var reason_=""

    override fun onItemClick(reason: String) {
        reason_=reason

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}