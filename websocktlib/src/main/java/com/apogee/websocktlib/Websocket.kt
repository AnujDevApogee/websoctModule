package com.apogee.websocktlib

import android.app.Activity
import com.apogee.websocktlib.utils.UtilsFiles
import java.lang.StringBuilder

class Websocket(activity: Activity) {

    private var BASE_URL = StringBuilder()

    companion object {
        private var INSTANCE: Websocket? = null
        fun getInstance(activity: Activity): Websocket {
            if (INSTANCE == null) {
                INSTANCE = Websocket(activity)
            }
            return INSTANCE!!
        }
    }


    fun newBuild(): Websocket {
        this.BASE_URL.clear()
        this.BASE_URL.append(UtilsFiles.WebSocketHeader)
        return this
    }

    fun addIpAddress(ip: String): Websocket {
        this.BASE_URL.append(ip)
        return this
    }

    fun addPort(port: String): Websocket {
        this.BASE_URL.append(":$port")
        return this
    }


    fun addBaseOrRover(name: String): Websocket {
        this.BASE_URL.append(UtilsFiles.WebSocketFolderPath)
        this.BASE_URL.append(name)
        return this
    }


    fun addMountPoint(rover: String): Websocket {
        this.BASE_URL.append("&")
        this.BASE_URL.append(rover)
        return this
    }

    fun build() {
        UtilsFiles.createLogCat("TAG_INFO", "${this.BASE_URL}")
        return
    }

    fun establishConnection() {

    }


    fun disconnect(){

    }

}