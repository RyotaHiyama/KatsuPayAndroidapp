package com.sample.katsupay

import android.content.Intent
import android.os.Bundle
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
    }
}