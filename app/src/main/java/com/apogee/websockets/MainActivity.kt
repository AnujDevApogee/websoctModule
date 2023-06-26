package com.apogee.websockets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.apogee.websockets.databinding.ActivityMainBinding
import com.apogee.websocktlib.Websocket

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webSocket=Websocket.getInstance(this)
        webSocket.newBuild()
            .addIpAddress("192.168.1.17")
            .addPort("8080")
            .addBaseOrRover("D_37")
            .addMountPoint("apogee1")
            .build()


    }
}