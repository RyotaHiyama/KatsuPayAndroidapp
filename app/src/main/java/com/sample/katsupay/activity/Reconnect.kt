package com.sample.katsupay.activity

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.R

import kotlinx.android.synthetic.main.reconnect.*

class Reconnect : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reconnect)

        reconnect.setOnClickListener {
            if(checkCommStatus()) {
                val intent = Intent(this, SignIn::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun checkCommStatus() : Boolean {
        val cm = getSystemService(ConnectivityManager::class.java) ?: return false
        if (cm.allNetworks.isNotEmpty()) {
            return true
        }
        return false
    }
}