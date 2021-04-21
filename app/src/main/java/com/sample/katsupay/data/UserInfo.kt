package com.sample.katsupay.data

import com.sample.katsupay.activity.Password
import com.sample.katsupay.communication.data.Customer

object UserInfo {
        private var password: Password? = null // パスワード
        var customer_id: String? = null // サーバアクセス用のID
        var email = "" // Eメールアドレス
        var birthday = "" // 誕生日

        fun initPassword(pass:String) : Boolean {
            return if(password != null) false
            else {
                password = Password(pass)
                true
            }
        }

        fun initialize(customer: Customer) {
            customer_id = customer.customerId
            email = customer.email
            birthday = customer.birthday
            initPassword(customer.password)
        }

        fun delete() {
            customer_id = null
            email = ""
            birthday = ""
            password = null
        }

        fun getPassword() : String {
            val pass = password ?: return ""
            return pass.getPassword()
        }
}