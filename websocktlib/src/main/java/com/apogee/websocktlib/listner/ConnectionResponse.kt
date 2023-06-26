package com.apogee.websocktlib.listner



sealed class ConnectionResponse {

    class OnOpen(val response: String) : ConnectionResponse()

    class OnClosed(val code: Int, val reason: String) : ConnectionResponse()

    class OnFailure(val throwable: Throwable) : ConnectionResponse()

    class OnMessage(val response: String) : ConnectionResponse()

}
