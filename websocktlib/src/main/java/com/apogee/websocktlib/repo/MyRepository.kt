package com.apogee.websocktlib.repo


import com.apogee.websocktlib.WebsocketConnection
import com.apogee.websocktlib.listner.ConnectionResponse
import com.apogee.websocktlib.listner.WebSocketListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MyRepository(private val webSocketAddress: String) {

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    var listenerForChange: Flow<ConnectionResponse>?=null

    private val webSocketListener = WebsocketConnection(
        object : WebSocketListener {
            override fun webSocketListener(conn: ConnectionResponse) {
                    listenerForChange= flow {
                        emit(conn)
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