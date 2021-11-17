package uz.tis.juft.ui.Main.Chat.Socket

import android.util.Log
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.util.*

object ChatSocket {
     var socket: Socket

    init {
        val options = IO.Options()
        options.path = "/socket.io"
        socket = IO.socket("http://188.120.232.38:3100", options)
        socket.connect()
        Log.d("eee", "init  ")
    }

//    fun setListennerSocket() {
//        ChatSocket.socket.on("update-chat-list", Emitter.Listener {
//           runOnUiThread {
//                var obj = it[0] as JSONObject
//
//                val messsage = obj.getJSONObject("message") as JSONObject
//                val created = messsage.getString("created")
//                val owner = messsage.getString("owner")
//                val viewed = messsage.getBoolean("viewed")
//                val _id = messsage.getString("_id")
//                val room = messsage.getString("room")
//                val text = messsage.getString("text")
//                adapter.sentMessage(NewMessageModel(0, _id, created, owner, room, text, viewed, ""))
//            }
//        })
//
//    }

}