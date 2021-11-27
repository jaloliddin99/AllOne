package com.tesseract.AllOneClient.fragments.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.tesseract.AllOneClient.adapter.ChatAdapter
import com.tesseract.AllOneClient.databinding.FragmentChatBinding
import com.tesseract.AllOneClient.model.chat.ChatWriteModel
import com.tesseract.AllOneClient.model.chat.Message
import com.tesseract.AllOneClient.services.SendMessageSocket
import com.tesseract.AllOneClient.services.SocketHandler
import com.tesseract.AllOneClient.utils.headerMapUniversal
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject


@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatWriteModel: ChatViewModel
    private lateinit var adapter: ChatAdapter
    private val args:ChatFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ChatAdapter(ArrayList())
        binding.recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        init()

        listener()

        SocketHandler.setSocket()
        SocketHandler.establishConnection()

        val mSocket = SocketHandler.getSocket()

        mSocket.on("chat_client_1") { args ->
            if (args[0] != null) {
                val counter = args[0] as ChatWriteModel
                activity?.runOnUiThread {
                    Log.d("@@@", "xxxxxx: $counter")

                    val message=Message(1,counter.content, "1111111112222212", "dc", 1, "text")
                    adapter.addMessage(message)
                    //binding.image.visibility=View.GONE

                }
            }
        }

        //uiOnItemClickListener...
        uiOnItemClickListener()

    }

    private fun listener() {
        chatWriteModel.chatModel.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE

            Picasso.get().load(it.content.driver_avatar).into(binding.imageAvater)
            binding.txtName.text = it.content.driver_name

            adapter=ChatAdapter(it.content.messages as ArrayList<Message>)
            binding.recyclerview.adapter = adapter

        })

        chatWriteModel.error.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun init() {
        chatWriteModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        chatWriteModel.getChatModel(headerMapUniversal(requireContext()), args.orderId.toString())
    }

    private fun uiOnItemClickListener() {
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        SendMessageSocket.setSendSocket()
        SendMessageSocket.establishConnection()
        val sendMessageSocket = SendMessageSocket.getSocket()


        binding.btnMessage.setOnClickListener {
            Log.d("@@@", "uiOnItemClickListener: ${sendMessageSocket.connected()}")
            val model = ChatWriteModel(1, binding.chatEdittext.text.toString(), "cd", 1, 1, "text")
            val message=Message(1,binding.chatEdittext.text.toString(), "1111111112222212", "cd", 1, "text")
            println(JSONObject(Gson().toJson(model)))
            sendMessageSocket.emit("chat_send", JSONObject(Gson().toJson(model)))
            binding.chatEdittext.setText("")
            adapter.addMessage(message)
            binding.recyclerview.smoothScrollToPosition(binding.recyclerview.adapter?.itemCount!!)
            //binding.image.visibility=View.GONE
        }
        onBackPassed()
    }

    private fun onBackPassed() {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                 findNavController().popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}