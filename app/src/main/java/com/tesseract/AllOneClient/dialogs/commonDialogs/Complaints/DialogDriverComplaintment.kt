package com.tesseract.AllOneClient.dialogs.commonDialogs.Complaints

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.dialogs.ComplaintOptionsAdapter
import com.tesseract.AllOneClient.databinding.DialogDriverComplaintmentBinding
import com.tesseract.AllOneClient.model.dialogComplaint.MakeComplaintPostBody
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogDriverComplaintment(private val type:String, private val driverId:Int,  private val orderId:Int):DialogFragment(),
    ComplaintOptionsAdapter.MakeComplaint {
    private lateinit var binding:DialogDriverComplaintmentBinding
    private lateinit var viewModel: DialogComplaintViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= DialogDriverComplaintmentBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(DialogComplaintViewModel::class.java)
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.bg_white_background);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.complaint(headerMapUniversal(requireContext()),type )
        binding.recyclerView.layoutManager=LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.complaintOptions.observe(viewLifecycleOwner, {
            binding.recyclerView.adapter= ComplaintOptionsAdapter(it, this)
            binding.recyclerView.setHasFixedSize(true)
        })

        binding.cancel.setOnClickListener {
            dialog?.cancel()
        }

        binding.send.setOnClickListener {
            val comment=binding.comment.text.toString()
            val body=MakeComplaintPostBody(
                comment,
                driverId,
                reasonFor
            )
            Log.i("TAG", "onViewCreated: comment  $comment driverId $driverId, reason for $reasonFor order id $orderId")
            viewModel.makeComplaintPost(headerMapUniversal(requireContext()), orderId,  driverId, reasonFor, comment)
        }
        viewModel.makeComplaintResponse.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            dialog?.dismiss()
        })
    }

    private var reasonFor=""


    override fun onItemClick(reason: String) {
        reasonFor=reason
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation;
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}