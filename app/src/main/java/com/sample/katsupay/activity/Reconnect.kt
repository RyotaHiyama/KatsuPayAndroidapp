package com.sample.katsupay.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.R
import com.sample.katsupay.communication.CommServer

import kotlinx.android.synthetic.main.reconnect.*

class Reconnect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reconnect)

        reconnect.setOnClickListener {
            if(CommServer.isConnected(this)) {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}