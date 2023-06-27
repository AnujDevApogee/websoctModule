package com.apogee.websockets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.apogee.websockets.data.GetResponse
import com.apogee.websockets.databinding.ActivityMainBinding
import com.apogee.websockets.utils.showToast
import com.apogee.websocktlib.Websocket
import com.apogee.websocktlib.builder.SocketBuilder
import com.apogee.websocktlib.listner.ConnectionResponse
import com.apogee.websocktlib.listner.WebSocketListener
import com.apogee.websocktlib.utils.UtilsFiles
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var webSocketClient: Websocket

    private val callBack = object : WebSocketListener {
        override fun webSocketListener(conn: ConnectionResponse) {
            when (conn) {
                is ConnectionResponse.OnDisconnect -> {
                    UtilsFiles.createLogCat("MAIN_Response", " onClosed => ${conn.code} and ${conn.reason}")
                    binding.msgDisplay.append("Disconnected")
                    binding.msgDisplay.append("\n")
                }

                is ConnectionResponse.OnResponseError -> {
                    binding.msgDisplay.append(conn.throwable.message)
                    binding.msgDisplay.append("\n")
                    UtilsFiles.createLogCat(
                        "MAIN_Response",
                        "OnFailure -> ${conn.throwable.localizedMessage} "
                    )
                }

                is ConnectionResponse.OnResponse -> {
                    try {
                        val obj=UtilsFiles.fromJson<GetResponse>(conn.response)
                        binding.msgDisplay.append("${if (obj.user == BASE_NAME) "You" else obj.user}:${obj.msg}")
                        binding.msgDisplay.append("\n")
                        UtilsFiles.createLogCat(
                            "MAIN_Response",
                            " MESSAGE-> ${conn.response}"
                        )
                    }catch (e:Exception){
                        UtilsFiles.createLogCat(
                            "MAIN_Response",
                            " MESSAGE-> ${conn.response}"
                        )
                    }

                }

                is ConnectionResponse.OnConnected -> {
                    binding.msgDisplay.append(conn.response)
                    binding.msgDisplay.append("\n")
                    UtilsFiles.createLogCat(
                        "MAIN_Response",
                        " OnOpen -> ${conn.response}"
                    )
                }

                is ConnectionResponse.OnNetworkConnection -> {
                    UtilsFiles.createLogCat("MAIN_Response","${conn.response} and ${conn.isConnected}")
                }

                is ConnectionResponse.OnRequestError -> {
                    UtilsFiles.createLogCat("MAIN_Response", conn.errorCause)
                }
            }
        }

    }
companion object{
    const val BASE_NAME="JBP"
    const val CONN_POINT="apogee1"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.msgDisplay.movementMethod = ScrollingMovementMethod()
        webSocketClient = SocketBuilder()
            .newBuild()
            .addCallback(callBack)
            .addIpAddress("192.168.1.37")
            .addPort("8080")
            .addBaseOrRover(BASE_NAME)
            .addMountPoint(CONN_POINT)
            .build()


        binding.connectionBtn.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    webSocketClient.establishConnection(this@MainActivity)
                }
            }
        }

        binding.disconnectBtn.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    webSocketClient.disconnect()
                }
            }
        }


        binding.sendBtn.setOnClickListener {
            val txt = binding.sendEdit.text?.toString()
            if (txt.isNullOrEmpty() || txt.isBlank()) {
                showToast("cannot be empty!!")
                return@setOnClickListener
            }
            val obj = GetResponse(BASE_NAME, txt, CONN_POINT)
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    webSocketClient.onRequestSent(UtilsFiles.toJson(obj)).also {
                        if (it){
                            binding.msgDisplay.append("${if (obj.user == BASE_NAME) "You" else obj.user}:${obj.msg}")
                            binding.msgDisplay.append("\n")
                        }
                    }
                }
            }
        }

        /* webSocket.newBuild()
             .addIpAddress("192.168.1.17")
             .addPort("8080")
             .addBaseOrRover("D_37")
             .addMountPoint("apogee1")
             .build()
 */

    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                webSocketClient.shutDown()
            }
        }
    }
}