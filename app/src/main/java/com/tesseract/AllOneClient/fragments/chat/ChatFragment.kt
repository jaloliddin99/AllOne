package com.tesseract.AllOneClient.fragments.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
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
import java.io.ByteArrayOutputStream


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

        SendMessageSocket.setSendSocket()
        SendMessageSocket.establishConnection()

        mSocket.on("chat_client_1") { args ->
            if (args[0] != null) {
                val counter = args[0] as JSONObject
                activity?.runOnUiThread {

                    val chatId=counter.getString("client_id").toInt()
                    val content=counter.getString("content")
                    val direction=counter.getString("direction")
                    val driver_id=counter.getString("driver_id").toInt()
                    val order_id=counter.getString("order_id")
                    val type=counter.getString("type")
                    if(type=="text"){

                        val message=Message(chatId,content,"1111111112222212",direction,driver_id,type)
                        adapter.addMessage(message)
                    }
                    else {
                        val message=Message(chatId,content,"1111111112222212",direction,driver_id,type)
                        adapter.addMessage(message)
                    }



                }
            }
        }

        uiOnItemClickListener()

    }

    private fun listener() {
        chatWriteModel.chatModel.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE

            Picasso.get().load(it.content.driver_avatar).into(binding.imageAvater)
            binding.txtName.text = it.content.driver_name


            println(it)
            adapter=ChatAdapter(it.content.messages as ArrayList<Message>)
            Toast.makeText(context, "${it.content.messages.size}", Toast.LENGTH_SHORT).show()
            binding.recyclerview.adapter = adapter

        })

        chatWriteModel.error.observe(requireActivity(), {
            binding.loader.loader.visibility=View.GONE
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun init() {
        chatWriteModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        chatWriteModel.getChatModel(headerMapUniversal(requireContext()), 1)
    }

    private fun uiOnItemClickListener() {
        binding.backToHome.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.uploadImage.setOnClickListener {
            openGallery()
        }


        val sendMessageSocket = SendMessageSocket.getSocket()


        binding.btnMessage.setOnClickListener {
            Log.d("@@@", "uiOnItemClickListener: ${sendMessageSocket.connected()}")
            val model = ChatWriteModel(1, binding.chatEdittext.text.toString(), "cd", 19, 1, "text")
            val message=Message(1,binding.chatEdittext.text.toString(), "1111111112222212", "cd", 1, "text")
            println(JSONObject(Gson().toJson(model)))
            sendMessageSocket.emit("chat_send", JSONObject(Gson().toJson(model)))
            binding.chatEdittext.setText("")
            adapter.addMessage(message)

            binding.recyclerview.smoothScrollToPosition(binding.recyclerview.adapter?.itemCount!!)
        }
        onBackPassed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {

                val selectedFile: Uri? = data!!.data

                if (data.clipData==null){
                    data.data.toString()
                    val message=Message(1,data.data.toString(), "1111111112222212", "cd", 1, "file")
                    adapter.addMessage(message)
                    binding.recyclerview.smoothScrollToPosition(binding.recyclerview.adapter?.itemCount!!)
                }


                if (selectedFile != null) {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedFile
                        )
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    val byteArray: ByteArray = outputStream.toByteArray()
                    val encodedString: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

                    val sendMessageSocket = SendMessageSocket.getSocket()

                    Log.d("@@@", "uiOnItemClickListener: ${sendMessageSocket.connected()}")
                    val model = ChatWriteModel(1, encodedString, "cd", 19, 1, "file")

                    println(JSONObject(Gson().toJson(model)))
                    sendMessageSocket.emit("chat_send", JSONObject(Gson().toJson(model)))
                    binding.chatEdittext.setText("")


                }

            }
        }
    }

    private var PICK_IMAGE_INTENT = 1

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_INTENT)

    }
    private fun onBackPassed() {

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}