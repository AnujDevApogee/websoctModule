package com.apogee.websocktlib



class Websocket(private val webSocketUrl: String) {

    companion object {
        private var INSTANCE: Websocket? = null
        fun getInstance(webSocketUrl: String): Websocket {
            if (INSTANCE == null) {
                INSTANCE = Websocket(webSocketUrl)
            }
            return INSTANCE!!
        }
    }



}