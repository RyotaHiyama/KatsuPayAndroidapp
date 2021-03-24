package com.sample.katsupay

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sample.katsupay.data.data.UserInfo
import kotlinx.android.synthetic.main.registration_information.*

class RegistrationInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_information)

        userName.text = "   " + UserInfo.customer_id
        mailAddress.text = "   " + UserInfo.email
        var birthdaySplit = UserInfo.birthday.split("-")
        try {
            birthday.text = "   ${birthdaySplit[0]}年${birthdaySplit[1]}月${birthdaySplit[2]}日"
        } catch( error:IndexOutOfBoundsException ){
            Toast.makeText(this, "登録情報を正常に取得できませんでした", Toast.LENGTH_LONG).show()
            finish()
        }

        RIReturnButton.setOnClickListener {
            finish()
        }
    }
}