package com.apogee.websocktlib.builder


import com.apogee.websocktlib.Websocket
import com.apogee.websocktlib.utils.UtilsFiles
import java.lang.StringBuilder

class SocketBuilder {
    private var BASE_URL = StringBuilder()



    fun newBuild(): SocketBuilder {
        this.BASE_URL.clear()
        this.BASE_URL.append(UtilsFiles.WebSocketHeader)
        return this
    }

    fun addIpAddress(ip: String): SocketBuilder {
        this.BASE_URL.append(ip)
        return this
    }

    fun addPort(port: String): SocketBuilder {
        this.BASE_URL.append(":$port")
        return this
    }


    fun addBaseOrRover(name: String): SocketBuilder {
        this.BASE_URL.append(UtilsFiles.WebSocketFolderPath)
        this.BASE_URL.append(name)
        return this
    }


    fun addMountPoint(rover: String): SocketBuilder {
        this.BASE_URL.append("&")
        this.BASE_URL.append(rover)
        return this
    }

    fun build(): Websocket {
        UtilsFiles.createLogCat("TAG_INFO", "${this.BASE_URL}")
        return Websocket(this.BASE_URL.toString())
    }
}