package com.sample.katsupay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.sign_up.*

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        SignUpReturn.setOnClickListener {
            finish()
        }

        SignUpDecide.setOnClickListener {
            Toast.makeText(this, "登録完了（テスト）", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}