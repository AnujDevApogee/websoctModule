package com.apogee.websocktlib

import com.apogee.websocktlib.listner.WebSocketListener
import com.apogee.websocktlib.repo.MyRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking


class Websocket(
    webSocketUrl: String,
    private val callback: WebSocketListener
) {
    private val repo = MyRepository(webSocketUrl)

    companion object {
        private var INSTANCE: Websocket? = null
        fun getInstance(webSocketUrl: String, callback: WebSocketListener): Websocket {
            if (INSTANCE == null) {
                INSTANCE = Websocket(webSocketUrl, callback)
            }
            return INSTANCE!!
        }
    }

    init {
        runBlocking {
            repo.listenerForChange?.collectLatest {
                callback.webSocketListener(it)
            }
        }
    }


    suspend fun establishConnection() {
        repo.createConnection()
    }

    suspend fun disconnect() {
        repo.disconnect()
    }

    suspend fun shutDown() {
        repo.shutdown()
    }

    suspend fun sendRequest(msg: String) {
        repo.rendRequest(msg)
    }

}