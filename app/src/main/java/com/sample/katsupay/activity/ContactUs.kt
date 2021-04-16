package com.sample.katsupay.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.R
import kotlinx.android.synthetic.main.contact_us.*

class ContactUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_us)

        returnBt.setOnClickListener {
            val signIn = Intent(this, SignIn::class.java)
            startActivity(signIn)
        }
    }
}