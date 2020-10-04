package com.sample.katsupay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        balance.text = "5,000円"

        purchaseHistoryButton.setOnClickListener {
            val intent = Intent(this, PurchaseHistory::class.java)
            startActivity(intent)
        }

        RIButton.setOnClickListener {
            val intent = Intent(this, RegistrationInformation::class.java)
            startActivity(intent)
        }

        update.setOnClickListener {
            Toast.makeText(this, "更新完了", Toast.LENGTH_LONG).show()
        }

        logout.setOnClickListener {
            val intent = Intent(this, SignInLayout::class.java)
            Toast.makeText(this, "ログアウトしました", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
    }

    fun update() {
        /* DBから最新の情報を取得するためのメソッド */
    }
}