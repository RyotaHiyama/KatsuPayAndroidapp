package com.sample.katsupay

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.registration_information.*

class RegistrationInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_information)

        userName.text = "   " + UserInfo.customer_id
        mailAddress.text = "   " + UserInfo.email
        var birthdaySplit = UserInfo.birthday.split("-")
        birthday.text = "   ${birthdaySplit[0]}年${birthdaySplit[1]}月${birthdaySplit[2]}日"

        RIReturnButton.setOnClickListener {
            finish()
        }
    }
}