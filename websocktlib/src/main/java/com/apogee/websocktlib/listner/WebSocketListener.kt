package com.apogee.websocktlib.listner

interface WebSocketListener {
    fun webSocketListener(conn:ConnectionResponse)
}