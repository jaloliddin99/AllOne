package com.tesseract.AllOneClient.fragments.taxiCity.saveUpdateLoc

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.databinding.FragmentSaveSelectedLocationBinding
import com.tesseract.AllOneClient.model.taxiCity.updateSaved.UpdateAddressBody
import com.tesseract.AllOneClient.utils.getNavOptions
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSaveUpdateLocation :Fragment() {
    private var _binding: FragmentSaveSelectedLocationBinding?=null
    private val binding get() = _binding!!
    private val args:FragmentSaveUpdateLocationArgs by navArgs()
    private lateinit var viewModel: CRUDLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSaveSelectedLocationBinding.inflate(inflater, container, false)
        viewModel=ViewModelProvider(this).get(CRUDLocationViewModel::class.java)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loader.loader.visibility=View.GONE
        binding.apply {
            if (args.shareSavedLoc!=null){
                deleteAddress.visibility=View.VISIBLE
                address.text=args.shareSavedLoc?.address
                placeName.setText(args.shareSavedLoc?.name)
            }else{
                deleteAddress.visibility=View.GONE
                address.text=args.address
            }

            backToHome.setOnClickListener {
                findNavController().navigate(R.id.action_global_saved_location_list, null, getNavOptions())
            }

            select.setOnClickListener {
                if (args.shareSavedLoc==null){

                    if (placeName.text.toString().isEmpty()){
                        Toast.makeText(context, "Please, enter note", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    postNewAddress(args.type, placeName.text.toString(), args.address, args.latLng)
                }else{
                    updateCurrent()
                }
            }

            deleteAddress.setOnClickListener {

                deleteCurrent()
            }

            viewModel.deleteSavedAddressObserver.observe(viewLifecycleOwner,  {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility=View.GONE
                findNavController().navigate(R.id.action_global_saved_location_list, null, getNavOptions())
            })

            viewModel.updateSavedAddressModel.observe(viewLifecycleOwner,  {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility=View.GONE
                findNavController().navigate(R.id.action_global_saved_location_list, null, getNavOptions())
            })

            viewModel.successM.observe(viewLifecycleOwner, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                binding.loader.loader.visibility=View.GONE
                findNavController().navigate(R.id.action_global_saved_location_list, null, getNavOptions())
            })
        }
    }

    private fun postNewAddress(type:String, name:String, address:String, latLng:String){
        val map = mutableMapOf<String, String>()
        map["type"]=type
        map["name"]=name
        map["address"]=address
        map["latlng"]=latLng
        binding.loader.loader.visibility=View.VISIBLE
        viewModel.storeNewAddress(headerMapUniversal(requireContext()), map)
    }

    private fun updateCurrent(){
        Log.i(TAG, "updateCurrent: ${args.shareSavedLoc?.latLng}  ${args.shareSavedLoc?.address}")
        val updateBOdy=UpdateAddressBody(
            args.shareSavedLoc?.address!!,
            args.shareSavedLoc?.latLng!!,
            binding.placeName.text.toString()
        )
        binding.loader.loader.visibility=View.VISIBLE

        viewModel.updateSavedAddress(headerMapUniversal(requireContext()), args.shareSavedLoc?.id!!, updateBOdy)
    }

    private fun deleteCurrent(){
        binding.loader.loader.visibility=View.VISIBLE
        viewModel.deleteSavedAddress(headerMapUniversal(requireContext()), args.shareSavedLoc?.id!!)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }



}