package com.apogee.websocktlib.repo


import com.apogee.websocktlib.WebsocketConnection
import com.apogee.websocktlib.listner.ConnectionResponse
import com.apogee.websocktlib.listner.WebSocketListener
import com.apogee.websocktlib.utils.UtilsFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MyRepository(
    private val webSocketAddress: String,
    private val listener: WebSocketListener
) {

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val webSocketListener = WebsocketConnection(
        object : WebSocketListener {
            override fun webSocketListener(conn: ConnectionResponse) {
                UtilsFiles.createLogCat("FLOW_VALUE", "$conn")
                runBlocking(Dispatchers.IO) {
                    listener.webSocketListener(conn)
                }
            }
        })


    fun createConnection() {

        this.webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
    }

    private fun createRequest(): Request {
        return Request.Builder()
            .url(webSocketAddress)
            .build()
    }

    fun rendRequest(request: String) {
        this.webSocket?.send(request)
    }

    fun disconnect() {
        this.webSocket?.close(1001, "Connection Closed")
    }


    fun shutdown() {
        okHttpClient.dispatcher.executorService.shutdown()
    }
}