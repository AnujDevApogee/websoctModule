package com.apogee.websocktlib.listner

import okhttp3.Response

sealed class ConnectionResponse {

    class OnOpen(val response: Response) : ConnectionResponse()

    class OnClosed(val code: Int, val reason: String) : ConnectionResponse()

    class OnFailure(val throwable: Throwable, val response: Response?) : ConnectionResponse()

    class OnMessage(val response: String) : ConnectionResponse()

}
