package com.sample.katsupay.datas.datas

import com.sample.katsupay.Password
import com.sample.katsupay.datas.transData.Customer

open class UserInfo {
    companion object {
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

        fun initialize(id:String, mail:String, birthday:String, pass:String) {
            this.customer_id = id
            this.email = mail
            this.birthday = birthday
            this.initPassword(pass)
        }

        fun initialize(customer: Customer) {
            this.customer_id = customer.customerId
            this.email = customer.email
            this.birthday = customer.birthday
            this.initPassword(customer.password)
        }

        fun delete() {
            this.customer_id = null
            this.email = ""
            this.birthday = ""
            this.password = null
        }

        fun getPassword() : String {
            val pass = password ?: return ""
            return pass.getPassword()
        }
    }
}