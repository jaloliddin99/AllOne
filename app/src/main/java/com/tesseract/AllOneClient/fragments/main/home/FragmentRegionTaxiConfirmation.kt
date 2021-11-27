package com.tesseract.AllOneClient.fragments.main.home

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tesseract.AllOneClient.databinding.FragmentRegionTaxiConfirmationBinding
import com.tesseract.AllOneClient.dialogs.sockets.DialogOrderCancelled
import com.tesseract.AllOneClient.model.taxiCity.tariffs.ListenOrderAccept
import com.tesseract.AllOneClient.services.SocketHandler

class FragmentRegionTaxiConfirmation: Fragment(), DialogOrderCancelled.OnLickListener {
    private var _binding: FragmentRegionTaxiConfirmationBinding? = null
    private val binding get() = _binding!!
    private val time= 20_000L
    private val args:FragmentRegionTaxiConfirmationArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentRegionTaxiConfirmationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.apply {
            val timer = object: CountDownTimer(time, 10*60*1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    val progress=(1-millisUntilFinished.toFloat()/time.toFloat())*100
                    wrongProgress.progress= progress

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

        mSocket.on("chat_client_2") { args ->
            if (args[0] != null) {
                val response = args[0] as ListenOrderAccept
                activity?.runOnUiThread {
                    if (response.status=="cancelled"){
                        DialogOrderCancelled(this).show(parentFragmentManager, tag)
                    }
                }
            }
        }


        binding.bookNow.setOnClickListener {
            val action= FragmentRegionTaxiConfirmationDirections.actionFragmentRegionTaxiConfirmationToFragmentRegionTaxiConfirmation2(args.tariff, args.orderId, args.driverId)
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