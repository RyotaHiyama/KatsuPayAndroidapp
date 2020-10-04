package com.sample.katsupay

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.sign_in.*

class SignInLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume(){
        super.onResume()

        setContentView(R.layout.sign_in)
        signIn.setOnClickListener {
            val signInOKIntent = Intent(this, SignInOKLayout::class.java)
            val password = passwordEdit.text.toString()
            val userName = userNameEdit.text.toString()
            signInOKIntent.putExtra("PASSWORD", password)
            signInOKIntent.putExtra("USERNAME", userName)
            startActivity(signInOKIntent)
        }

        SignUpButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        if(!checkCommStatus()) {
            val intent = Intent(this, Reconnect::class.java)
            startActivity(intent)
        }
    }



    private fun checkCommStatus() : Boolean {
        val cm = getSystemService(ConnectivityManager::class.java)
        if (cm.allNetworks.isNotEmpty()) {
            return true
        }
        return false
    }
}