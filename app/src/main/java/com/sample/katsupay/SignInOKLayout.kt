package com.sample.katsupay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.signin_ok_layout.*

class SignInOKLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signin_ok_layout)

        signInResult1.text = "${intent.getStringExtra("USERNAME")}さん．ようこそ"
        signInResult2.text = "サインインをしています．"

        signInLoadProgressBar?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}