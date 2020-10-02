package com.sample.katsupay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.registration_information.*

class RegistrationInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_information)

        RIReturnButton.setOnClickListener {
            finish()
        }
    }
}