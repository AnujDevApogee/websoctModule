package com.apogee.websocktlib.utils

import android.util.Log

object UtilsFiles {
    const val WebSocketHeader = "ws://"
    const val WebSocketFolderPath = "/WebSocket/"

    fun createLogCat(tag: String, msg: String) {
        Log.i(tag, "createLogCat: $msg")
    }
}