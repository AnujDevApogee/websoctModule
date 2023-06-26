package com.apogee.websocktlib.utils

import android.util.Log
import com.google.gson.Gson

object UtilsFiles {
    const val WebSocketHeader = "ws://"
    const val WebSocketFolderPath = "/WebSocket/"

    fun createLogCat(tag: String, msg: String) {
        Log.i(tag, "createLogCat: $msg")
    }
    fun <T> toJson(t: T): String {
        return Gson().toJson(t)
    }


    inline fun <reified T> fromJson(str: String): T {
        return Gson().fromJson(str, T::class.java)
    }
}


