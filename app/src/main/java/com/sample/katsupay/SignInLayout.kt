package com.sample.katsupay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.sign_in.*

class SignInLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)
    }

    override fun onResume(){
        super.onResume()

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
    }
}