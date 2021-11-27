package com.tesseract.AllOneClient.services

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SendMessageSocket {
    lateinit var sendSocket: Socket

    @Synchronized
    fun setSendSocket() {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use the your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            sendSocket = IO.socket("http://188.120.232.38:3102")
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return sendSocket
    }

    @Synchronized
    fun establishConnection() {
        sendSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        sendSocket.disconnect()
    }
}