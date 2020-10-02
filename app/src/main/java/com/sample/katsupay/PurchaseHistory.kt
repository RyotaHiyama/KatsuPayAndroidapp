package com.sample.katsupay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.purchase_history.*

class PurchaseHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_history)

        PurchaseReturnButton.setOnClickListener {
            finish()
        }
    }
}