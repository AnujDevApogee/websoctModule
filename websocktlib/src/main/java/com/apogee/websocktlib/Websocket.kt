package com.apogee.websocktlib

import android.content.Context
import com.apogee.websocktlib.listner.ConnectionResponse
import com.apogee.websocktlib.listner.WebSocketListener
import com.apogee.websocktlib.repo.MyRepository
import com.apogee.websocktlib.utils.UtilsFiles
import com.apogee.websocktlib.utils.isNetworkAvailable


@Suppress("RedundantSuspendModifier")
class Websocket(
    webSocketUrl: String,
    private val callback: WebSocketListener
) {
    private val repo = MyRepository(webSocketUrl, callback)

    companion object {
        private var INSTANCE: Websocket? = null
        fun getInstance(webSocketUrl: String, callback: WebSocketListener): Websocket {
            if (INSTANCE == null) {
                INSTANCE = Websocket(webSocketUrl, callback)
            }
            return INSTANCE!!
        }
    }


    suspend fun establishConnection(context: Context) {
        UtilsFiles.createLogCat("NETWORK_CONNECT","${context.isNetworkAvailable()}")
        if (!context.isNetworkAvailable()) {
            callback.webSocketListener(
                ConnectionResponse.OnNetworkConnection(
                    "No Internet connection found!!",
                    isConnected = context.isNetworkAvailable()
                )
            )
            return
        }
        repo.createConnection()
    }

    suspend fun disconnect() {
        repo.disconnect()
    }

    suspend fun shutDown() {
        repo.shutdown()
    }

    suspend fun onRequestSent(msg: String): Boolean {
        if (UtilsFiles.checkValue(msg)) {
            callback.webSocketListener(ConnectionResponse.OnRequestError("Invalid Request Body"))
            return false
        }
        repo.rendRequest(msg)
        return true
    }

}