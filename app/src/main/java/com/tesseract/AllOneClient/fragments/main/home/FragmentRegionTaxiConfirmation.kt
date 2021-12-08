package com.tesseract.AllOneClient.fragments.main.home

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.R
import com.tesseract.AllOneClient.constants.SaveData
import com.tesseract.AllOneClient.databinding.FragmentRegionTaxiConfirmationBinding
import com.tesseract.AllOneClient.dialogs.sockets.DialogOrderCancelled
import com.tesseract.AllOneClient.fragments.parcel.parcelMain.FragmentParcelMainDirections
import com.tesseract.AllOneClient.model.taxiCity.tariffs.ListenOrderAccept
import com.tesseract.AllOneClient.services.SocketHandler
import com.tesseract.AllOneClient.utils.statusBarColor
import org.json.JSONObject

class FragmentRegionTaxiConfirmation: Fragment(), DialogOrderCancelled.OnLickListener {
    private var _binding: FragmentRegionTaxiConfirmationBinding? = null
    private val binding get() = _binding!!
    private val time= 10*60*1000L
    private val args:FragmentRegionTaxiConfirmationArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentRegionTaxiConfirmationBinding.inflate(inflater, container, false)
        activity?.statusBarColor(
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            ResourcesCompat.getColor(resources, R.color.white, activity?.theme),
            true
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val timer = object: CountDownTimer(time, 100) {
                override fun onTick(millisUntilFinished: Long) {
                    val progress=(1.0-millisUntilFinished.toDouble()/time.toDouble())*100

                    wrongProgress.progress= progress.toFloat()
                }

                override fun onFinish() {
                    wrongProgress.progress=100f
                }
            }
            timer.start()
        }

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()

        mSocket.on("client_order_${args.orderId}") { args ->
            if (args[0] != null) {
                val response = args[0] as JSONObject
                activity?.runOnUiThread {
                    val direction=response.getString("status")
                    if (direction=="cancelled"){
                        DialogOrderCancelled(this).show(parentFragmentManager, tag)
                    }
                    if (direction=="driver_appointed"){
                        val action=FragmentRegionTaxiConfirmationDirections.actionFragmentRegionTaxiConfirmationToFragmentRegionTaxiConfirmation2(this.args.tariff, this.args.orderId, this.args.driverId)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            }
            )



        binding.bookNow.setOnClickListener {
            val action=FragmentRegionTaxiConfirmationDirections.actionGlobalCancelOrder(args.orderId, "interarea")
            findNavController().navigate(action)
        }



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun orderAgain() {

    }
}