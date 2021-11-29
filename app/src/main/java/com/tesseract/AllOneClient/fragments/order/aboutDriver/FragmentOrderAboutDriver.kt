package com.tesseract.AllOneClient.fragments.order.aboutDriver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.adapter.order.AboutDriverCarImagesAdapter
import com.tesseract.AllOneClient.databinding.FragmentOrderAboutDriverBinding
import com.tesseract.AllOneClient.dialogs.commonDialogs.Complaints.DialogDriverComplaintment
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrderAboutDriver: Fragment() {
    var _binding: FragmentOrderAboutDriverBinding?=null
    val binding get() = _binding!!
    val args: FragmentOrderAboutDriverArgs by navArgs()
    private lateinit var aboutDriverCarImagesAdapter: AboutDriverCarImagesAdapter
    private lateinit var viewModel: AboutDriverViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOrderAboutDriverBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(AboutDriverViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAboutDriver(headerMapUniversal(requireContext()), args.driverId)

        binding.apply {
            reusable.cancel.setImageResource(R.drawable.ic_attention)
            reusable.textCancel.text=getString(R.string.complain)
            reusable.cancel.setOnClickListener {
                val dialog=DialogDriverComplaintment(args.orderType,args.driverId, args.orderId)
                        dialog.isCancelable=false
                dialog.show(parentFragmentManager, tag)
            }

            backToHome.setOnClickListener {
                reusable.textCancel.text=getString(R.string.cancel)
                reusable.cancel.setImageResource(R.drawable.ic_ix_shape)
                findNavController().popBackStack()
            }

            requireActivity()
                .onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        reusable.textCancel.text=getString(R.string.cancel)
                        binding.reusable.cancel.setImageResource(R.drawable.ic_ix_shape)
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }
                })

            back.setOnClickListener {
                reusable.textCancel.text=getString(R.string.cancel)
                reusable.cancel.setImageResource(R.drawable.ic_ix_shape)
                findNavController().popBackStack()
            }
        }

        viewModel.aboutDriverData.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            binding.car.text=it.car
            driverId= it.id!!
            binding.name.text=it.name
            binding.rating.text=it.rating.toString()
            binding.phoneNumber.text=it.phoneNumber
            binding.avatar.let { it1 -> Glide.with(requireContext()).load(it.avatar).into(it1) }

            aboutDriverCarImagesAdapter= it.carPhotos?.let { it1 -> AboutDriverCarImagesAdapter(it1) }!!
            binding.postCarPhotos.layoutManager=LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.postCarPhotos.adapter=aboutDriverCarImagesAdapter
            binding.postCarPhotos.setHasFixedSize(true)

        })

        viewModel.errorMessage.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE

            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

        })


        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    private var driverId=-1


}