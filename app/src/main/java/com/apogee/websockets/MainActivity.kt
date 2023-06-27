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
                is ConnectionResponse.OnClosed -> {
                    UtilsFiles.createLogCat("MAIN_Response", " onClosed => ${conn.code} and ${conn.reason}")
                    binding.msgDisplay.append("Disconnected")
                    binding.msgDisplay.append("\n")
                }

                is ConnectionResponse.OnFailure -> {
                    binding.msgDisplay.append(conn.throwable.localizedMessage)
                    binding.msgDisplay.append("\n")
                    UtilsFiles.createLogCat(
                        "MAIN_Response",
                        "OnFailure -> ${conn.throwable.localizedMessage} "
                    )
                }

                is ConnectionResponse.OnMessage -> {
                    try {
                        val obj=UtilsFiles.fromJson<GetResponse>(conn.response)
                        binding.msgDisplay.append("${if (obj.user == "apogee1") "You" else obj.user}:${obj.msg}")
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

                is ConnectionResponse.OnOpen -> {
                    binding.msgDisplay.append(conn.response)
                    binding.msgDisplay.append("\n")
                    UtilsFiles.createLogCat(
                        "MAIN_Response",
                        " OnOpen -> ${conn.response}"
                    )
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.msgDisplay.movementMethod = ScrollingMovementMethod()
        webSocketClient = SocketBuilder()
            .newBuild()
            .addCallback(callBack)
            .addIpAddress("192.168.1.17")
            .addPort("8080")
            .addBaseOrRover("apogee1")
            .build()


        binding.connectionBtn.setOnClickListener {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    webSocketClient.establishConnection()
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
            val obj = GetResponse("apogee1", txt, "")
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    webSocketClient.sendRequest(UtilsFiles.toJson(obj))
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