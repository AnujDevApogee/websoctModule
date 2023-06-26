package com.apogee.websockets.data

import com.google.gson.annotations.SerializedName

data class GetResponse(
    @SerializedName("user")
    val user: String,
    @SerializedName("message")
    val msg: String,
    @SerializedName("mountPoint")
    val mountPoint:String
)
