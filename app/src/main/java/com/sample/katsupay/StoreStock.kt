package com.sample.katsupay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.store_stock.*

class StoreStock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_stock)
        StoStockReturnButton.setOnClickListener{
            finish()
        }
    }
}