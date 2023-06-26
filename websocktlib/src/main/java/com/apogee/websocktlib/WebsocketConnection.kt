package com.apogee.websocktlib

import com.apogee.websocktlib.listner.ConnectionResponse
import com.apogee.websocktlib.utils.UtilsFiles
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebsocketConnection(private val listener: com.apogee.websocktlib.listner.WebSocketListener) :
    WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        UtilsFiles.createLogCat("WEB_SOCKET", "$response")
        listener.webSocketListener(ConnectionResponse.OnOpen(response))
    }


    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        UtilsFiles.createLogCat("WEB_SOCKET", "$code and $reason")
        listener.webSocketListener(ConnectionResponse.OnClosed(code, reason))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        UtilsFiles.createLogCat("WEB_SOCKET", "${t.message} ${response?.message}")
        listener.webSocketListener(ConnectionResponse.OnFailure(t, response))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        UtilsFiles.createLogCat("WEB_SOCKET", text)
        listener.webSocketListener(ConnectionResponse.OnMessage(text))
    }
}